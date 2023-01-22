package com.notesapp.statistic.controller;

import com.notesapp.model.view.NoteView;
import com.notesapp.statistic.service.NotesAppServerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
public class NotesAppServerController {

    private final NotesAppServerClient notesAppServerClient;

    public NotesAppServerController(NotesAppServerClient notesAppServerClient) {
        this.notesAppServerClient = notesAppServerClient;
    }

    @GetMapping(path = "/notes", produces = "application/json")
    public NoteView getRandomNote() {
        return notesAppServerClient.getRandomNote();
    }

    @GetMapping(path = "/greetings")
    public String getGreetingsFromPython() {
        return notesAppServerClient.getGreetingsFromPython();
    }
}
