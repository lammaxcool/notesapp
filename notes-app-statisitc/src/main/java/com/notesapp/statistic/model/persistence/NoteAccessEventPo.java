package com.notesapp.statistic.model.persistence;

import com.notesapp.statistic.model.event.EventType;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NoteAccessEvents")
public class NoteAccessEventPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long noteId;

    @Column(nullable = false)
    private EventType eventType;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long noteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public EventType eventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NoteAccessEventPo that = (NoteAccessEventPo) o;
        return Objects.equals(id, that.id) && Objects.equals(noteId, that.noteId) && eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noteId, eventType);
    }
}
