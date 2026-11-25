package com.example.memo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter // class, getter
public class Memo {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // DB의 pk야.
    private String title;
    private String content;

    protected Memo() {}
    public Memo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
