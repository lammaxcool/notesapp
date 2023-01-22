package com.notesapp.controller;

import com.notesapp.model.view.NoteView;
import com.notesapp.service.NotesService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @RateLimiter(name = "rateLimiterGetAll")
    @GetMapping(produces = "application/json")
    public List<NoteView> getAllNotes() {
        return notesService.getAllNotes();
    }

    @GetMapping(path = "/random", produces = "application/json")
    public NoteView getRandomNote() {
        return notesService.getRandomNote();
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

    @ExceptionHandler({RequestNotPermitted.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public void handleRequestNotPermitted() {
    }
}