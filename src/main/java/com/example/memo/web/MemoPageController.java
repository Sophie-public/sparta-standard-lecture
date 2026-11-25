package com.example.memo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/memos-page")
public class MemoPageController {

    @GetMapping
    public String showMemos() {
        // View의 이름으로, templates/memos.html을 사용해서 화면을 만듦.
        return "memos";
    }




}
