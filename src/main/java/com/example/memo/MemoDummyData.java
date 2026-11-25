package com.example.memo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemoDummyData implements CommandLineRunner {

    private final MemoRepository repository;

    public MemoDummyData(MemoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {

        repository.saveAll(List.of(new Memo("Spring 시작하기1", "메모 내용1"),
                new Memo("Spring 시작하기2", "메모 내용2"),
                new Memo("Spring 시작하기3", "메모 내용3"),
                new Memo("Spring 시작하기4", "메모 내용4"),
                new Memo("Spring 시작하기5", "메모 내용5"),
                new Memo("Java 시작하기1", "메모 내용1"),
                new Memo("Java 시작하기2", "메모 내용2")
        ));
    }
}
