package com.notesapp.notesapp.model.view;

import com.notesapp.notesapp.model.persistence.NotePo;

public record NoteView(Long id, String title, String content) {

    public NoteView(String title, String content) {
        this(null, title, content);
    }

    public NoteView(NotePo notePo) {
        this(notePo.getId(), notePo.getTitle(), notePo.getContent());
    }
}