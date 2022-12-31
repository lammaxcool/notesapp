package com.notesapp.notesapp.service;

import com.notesapp.notesapp.model.persistence.NotePo;
import com.notesapp.notesapp.model.view.NoteView;
import com.notesapp.notesapp.repository.NotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotesServiceTest {

    @Mock
    NotesRepository notesRepository;

    NotesService notesService;

    @BeforeEach
    void setUp() {
        notesService = new NotesService(notesRepository);
    }

    @Test
    void createNote() {
        when(notesRepository.save(new NotePo("title", "content"))).thenReturn(new NotePo(1L, "title", "content"));

        var noteToCreate = new NoteView("title", "content");

        var createdNote = notesService.createNote(noteToCreate);
        assertThat(createdNote.id()).isNotNull();
        assertThat(createdNote.title()).isEqualTo(noteToCreate.title());
        assertThat(createdNote.content()).isEqualTo(noteToCreate.content());
    }

    @Test
    void getNote() {
        var notePo = new NotePo(1L, "title", "content");
        when(notesRepository.findById(1L)).thenReturn(Optional.of(notePo));

        var note = notesService.getNoteById(1L);

        assertThat(note.id()).isEqualTo(notePo.getId());
        assertThat(note.title()).isEqualTo(notePo.getTitle());
        assertThat(note.content()).isEqualTo(notePo.getContent());

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

    }

    @Test
    void getNote_whenNoteIsNotFound() {
        when(notesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> notesService.getNoteById(1L));
    }

    @Test
    void updateNote() {
        var updatedNoteView = new NoteView(1L, "new content", "new title");
        var updatedNotePo = new NotePo(1L, "new content", "new title");

        when(notesRepository.existsById(1L)).thenReturn(true);
        when(notesRepository.save(updatedNotePo)).thenReturn(updatedNotePo);

        var updatedNote = notesService.updateNote(updatedNoteView);

        assertThat(updatedNote.id()).isEqualTo(updatedNoteView.id());
        assertThat(updatedNote.title()).isEqualTo(updatedNoteView.title());
        assertThat(updatedNote.content()).isEqualTo(updatedNoteView.content());
    }

    @Test
    void updateNote_whenNoteIsNotFound() {
        when(notesRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> notesService.updateNote(new NoteView(1L, "", "")));
    }

    @Test
    void deleteNote() {
        when(notesRepository.existsById(1L)).thenReturn(true);

        notesService.deleteNoteById(1L);

        verify(notesRepository).deleteById(1L);
    }

    @Test
    void deleteNote_whenNoteIsNotFound() {
        when(notesRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> notesService.deleteNoteById(1L));
    }
}