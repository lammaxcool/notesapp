package com.notesapp.model.event;

import com.notesapp.model.view.NoteView;

public record NoteAccessEvent(NoteView note, EventType event) {
}