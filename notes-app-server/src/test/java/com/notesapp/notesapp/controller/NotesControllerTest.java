package com.notesapp.notesapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.notesapp.notesapp.model.view.NoteView;
import com.notesapp.notesapp.service.NotesService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(controllers = {NotesController.class})
@ContextConfiguration(classes = {NotesController.class})
class NotesControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    NotesService notesService;

    @Test
    void createNote() throws Exception {
        when(notesService.createNote(any(NoteView.class))).thenReturn(new NoteView(1L, "title", "content"));

        mvc.perform(post("/notes/")
                        .contentType(APPLICATION_JSON)
                        .content("{"
                                + "\"title\":\"title\","
                                + "\"content\":\"content\""
                                + "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.title", Matchers.is("title")))
                .andExpect(jsonPath("$.content", Matchers.is("content")));
    }

    @Test
    void getNote() throws Exception {
        when(notesService.getNoteById(anyLong())).thenReturn(new NoteView(1L, "title", "content"));

        mvc.perform(get("/notes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.title", Matchers.is("title")))
                .andExpect(jsonPath("$.content", Matchers.is("content")));
    }

    @Test
    void getAllNotes() throws Exception {
        when(notesService.getAllNotes()).thenReturn(List.of(new NoteView(1L, "title", "content")));

        mvc.perform(get("/notes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].title", Matchers.is("title")))
                .andExpect(jsonPath("$[0].content", Matchers.is("content")));
    }

    @Test
    void updateNote() throws Exception {
        when(notesService.updateNote(new NoteView(1L, "new title", "new content"))).thenReturn(new NoteView(1L, "new title", "new content"));

        mvc.perform(put("/notes/edit")
                        .contentType(APPLICATION_JSON)
                        .content("{"
                                + "\"id\":1,"
                                + "\"title\":\"new title\","
                                + "\"content\":\"new content\""
                                + "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.title", Matchers.is("new title")))
                .andExpect(jsonPath("$.content", Matchers.is("new content")));
    }

    @Test
    void deleteNote() throws Exception {
        mvc.perform(delete("/notes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(1)));
    }
}