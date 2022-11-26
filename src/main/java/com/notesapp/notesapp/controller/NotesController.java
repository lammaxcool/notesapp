package com.notesapp.notesapp.controller;

import com.notesapp.notesapp.model.view.NoteView;
import com.notesapp.notesapp.service.NotesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public NoteView createNote(@RequestBody NoteView noteView) {
        return notesService.createNote(noteView);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public NoteView getNote(@PathVariable Long id) {
        return notesService.getNoteById(id);
    }

    @GetMapping(produces = "application/json")
    public List<NoteView> getAllNotes() {
        return notesService.getAllNotes();
    }

    @PutMapping(path = "/edit", consumes = "application/json", produces = "application/json")
    public NoteView updateNote(@RequestBody NoteView noteView) {
        return notesService.updateNote(noteView);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteNote(@PathVariable Long id) {
        notesService.deleteNoteById(id);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}