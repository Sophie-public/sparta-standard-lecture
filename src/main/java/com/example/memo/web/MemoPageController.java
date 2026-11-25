package com.example.memo.web;

import com.example.memo.MemoRepository;
import com.example.memo.MemoResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/memos-page")
public class MemoPageController {

    private final MemoRepository repository;

    public MemoPageController(MemoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String showMemos(Model model) {
        // View의 이름으로, templates/memos.html을 사용해서 화면을 만듦.
        List<MemoResponse> memos = repository.findAll().stream().map(MemoResponse::from).toList();
        model.addAttribute("memos", memos);

        return "memos";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model
    ) {

        if (page < 0 || size < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "page는 0 이상, size는 1 이상이어야 합니다."
            );
        }

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by("id").ascending()
        );

        List<MemoResponse> memos = repository.findByTitleContaining(keyword, pageRequest)
                .stream()
                .map(MemoResponse::from)
                .toList();

        model.addAttribute("memos", memos);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "memos";
    }
}
