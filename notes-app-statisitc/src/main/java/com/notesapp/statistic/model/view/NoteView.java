package com.notesapp.statistic.model.view;


public record NoteView(Long id, String title, String content) {

    public NoteView(String title, String content) {
        this(null, title, content);
    }

}