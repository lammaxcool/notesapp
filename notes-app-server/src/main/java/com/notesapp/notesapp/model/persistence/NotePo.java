package com.notesapp.notesapp.model.persistence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Notes")
public class NotePo {

    public NotePo() {
    }

    public NotePo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NotePo(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotePo notePo = (NotePo) o;
        return Objects.equals(id, notePo.id) && Objects.equals(title, notePo.title) && Objects.equals(content, notePo.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content);
    }
}