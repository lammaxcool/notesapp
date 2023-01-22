package com.notesapp.statistic.model.persistence;

import com.notesapp.model.event.EventType;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Events")
public class NoteAccessEventPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long note;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    public NoteAccessEventPo() {
    }

    public NoteAccessEventPo(Long note, EventType type) {
        this.note = note;
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getNote() {
        return note;
    }

    public void setNote(Long noteId) {
        this.note = noteId;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType eventType) {
        this.type = eventType;
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
        return Objects.equals(id, that.id) && Objects.equals(note, that.note) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note, type);
    }
}
