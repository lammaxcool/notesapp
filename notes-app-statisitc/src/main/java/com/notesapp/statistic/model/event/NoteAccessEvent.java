package com.notesapp.statistic.model.event;

import com.notesapp.statistic.model.view.NoteView;

public record NoteAccessEvent(NoteView note, EventType event) {
}