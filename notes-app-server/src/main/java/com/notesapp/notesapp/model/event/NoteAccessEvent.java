package com.notesapp.notesapp.model.event;

import com.notesapp.notesapp.model.view.NoteView;

public record NoteAccessEvent(NoteView note, EventType event) {
}