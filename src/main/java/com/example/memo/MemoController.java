package com.example.memo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/memos")
public class MemoController {

    private final MemoRepository repository;

    // 생성자 주입
    public MemoController(MemoRepository memoRepository) {
        this.repository = memoRepository;
    }

    // CRUD
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // 생성 성공시 : 201 Created 반환
    public MemoResponse create(@RequestBody MemoRequest request) {
        // create 하면 해야될 동작
        // H2 DB가 있는데, 거기 테이블에 넣어야함.
        Memo memo = repository.save(new Memo(request.title(), request.content()));
        return MemoResponse.from(memo);
    }

    // read
    @GetMapping
    public List<MemoResponse> findAll() {
        return repository.findAll().stream()
                .map(MemoResponse::from).toList();
    }

    // 1-read
    @GetMapping("/{id}")
    public MemoResponse findOne(@PathVariable Long id){

        Memo memo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "메모가 없습니다."
                        )

                );

        return MemoResponse.from(memo);
    }

    // update
    // 2번 메모를 가져와서 내가 json에 써놓은 title과 content로 바꿔줘.
    @PutMapping("/{id}")
    @Transactional
    public MemoResponse update(@PathVariable Long id, @RequestBody MemoRequest request) {

        Memo memo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "메모가 없다."
                        )

                );

        memo.update(request.title(), request.content());
        return MemoResponse.from(memo);
    }

    // delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // 204 No content
    public void delete(@PathVariable Long id) {
        // 일단 db에 존재하는지 확인
        extracted(id);
        repository.deleteById(id);
    }

    private void extracted(Long id) {
        if(!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제하는데 메모가없대.");
        }
    }

    @GetMapping("/search")
    public List<MemoResponse> search(
            @RequestParam(name = "keyword") String keyword) {

        return repository.findAll().stream()
                .filter(memo -> memo.getTitle() != null
                        && memo.getTitle().contains(keyword))
                .map(MemoResponse::from)
                .toList();


    }

    @GetMapping("/searchv2")
    public List<MemoResponse> search(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        if (page < 0 || size < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "page는 0 이상, size는 1 이상이어야 합니다."
            );
        }

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by("id").ascending()
        );

        return repository.findByTitleContaining(keyword, pageRequest)
                .stream()
                .map(MemoResponse::from)
                .toList();
    }
}
