package com.notesapp.notesapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.notesapp.notesapp.model.persistence.NotePo;
import com.notesapp.notesapp.model.view.NoteView;
import com.notesapp.notesapp.repository.NotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class NotesServiceTest {

    @Mock
    NotesRepository notesRepository;
    @Mock
    NotificationService<NoteView> notificationService;

    NotesService notesService;

    @BeforeEach
    void setUp() {
        notesService = new NotesService(notesRepository, notificationService);
    }

    @Test
    void createNote() {
        when(notesRepository.save(new NotePo("title", "content"))).thenReturn(new NotePo(1L, "title", "content"));

        var noteToCreate = new NoteView("title", "content");

        var createdNote = notesService.createNote(noteToCreate);
        assertThat(createdNote.id()).isNotNull();
        assertThat(createdNote.title()).isEqualTo(noteToCreate.title());
        assertThat(createdNote.content()).isEqualTo(noteToCreate.content());
        verify(notificationService).onCreate(createdNote);
    }

    @Test
    void getNote() {
        var notePo = new NotePo(1L, "title", "content");
        when(notesRepository.findById(1L)).thenReturn(Optional.of(notePo));

        var note = notesService.getNoteById(1L);

        assertThat(note.id()).isEqualTo(notePo.getId());
        assertThat(note.title()).isEqualTo(notePo.getTitle());
        assertThat(note.content()).isEqualTo(notePo.getContent());
        verify(notificationService).onRead(note);
    }

    @Test
    void getAllNotes() {
        var notePo = new NotePo(1L, "title", "content");
        when(notesRepository.findAll()).thenReturn(List.of(notePo));

        var notes = notesService.getAllNotes();

        assertThat(notes).hasSize(1)
                .allSatisfy(note -> {
                    assertThat(note.id()).isEqualTo(notePo.getId());
                    assertThat(note.title()).isEqualTo(notePo.getTitle());
                    assertThat(note.content()).isEqualTo(notePo.getContent());
                });
        verify(notificationService).onRead(new NoteView(notePo));
    }

    @Test
    void getNote_whenNoteIsNotFound() {
        when(notesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> notesService.getNoteById(1L));
    }

    @Test
    void updateNote() {
        var updatedNotePo = new NotePo(1L, "new content", "new title");
        var updatedNoteView = new NoteView(updatedNotePo);

        when(notesRepository.existsById(1L)).thenReturn(true);
        when(notesRepository.save(updatedNotePo)).thenReturn(updatedNotePo);

        var updatedNote = notesService.updateNote(updatedNoteView);

        assertThat(updatedNote.id()).isEqualTo(updatedNoteView.id());
        assertThat(updatedNote.title()).isEqualTo(updatedNoteView.title());
        assertThat(updatedNote.content()).isEqualTo(updatedNoteView.content());
        verify(notificationService).onUpdate(updatedNote);
    }

    @Test
    void updateNote_whenNoteIsNotFound() {
        when(notesRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> notesService.updateNote(new NoteView(1L, "", "")));
    }

    @Test
    void deleteNote() {
        var notePo = new NotePo(1L, "new content", "new title");
        when(notesRepository.findById(1L)).thenReturn(Optional.of(notePo));

        notesService.deleteNoteById(1L);

        verify(notesRepository).deleteById(1L);
        verify(notificationService).onDelete(new NoteView(notePo));
    }

    @Test
    void deleteNote_whenNoteIsNotFound() {
        when(notesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> notesService.deleteNoteById(1L));
    }
}