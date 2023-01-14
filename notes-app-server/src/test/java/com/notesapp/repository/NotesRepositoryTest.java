package com.notesapp.repository;

import com.notesapp.model.persistence.NotePo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class NotesRepositoryTest {

    @Autowired
    NotesRepository notesRepository;

    @Test
    void save() {
        NotePo note = new NotePo("title", "content");

        notesRepository.saveAndFlush(note);

        List<NotePo> notes = notesRepository.findAll();
        assertThat(notes).hasSize(1)
                .allSatisfy(it -> {
                    assertThat(it.getTitle()).isEqualTo(note.getTitle());
                    assertThat(it.getContent()).isEqualTo(note.getContent());
                });
    }

    @Test
    void delete() {
        NotePo note = new NotePo("title", "content");

        var persistedNote = notesRepository.saveAndFlush(note);
        notesRepository.deleteById(persistedNote.getId());

        assertThat(notesRepository.findAll()).isEmpty();
    }

    @Test
    void update() {
        NotePo note = new NotePo("title", "content");

        var persistedNote = notesRepository.saveAndFlush(note);

        persistedNote.setTitle("new title");
        notesRepository.saveAndFlush(persistedNote);

        List<NotePo> notes = notesRepository.findAll();
        assertThat(notes).hasSize(1)
                .allSatisfy(it -> {
                    assertThat(it.getTitle()).isEqualTo(note.getTitle());
                    assertThat(it.getContent()).isEqualTo(persistedNote.getContent());
                });
    }

    @Test
    void existsById() {
        NotePo note = new NotePo("title", "content");

        var persistedNote = notesRepository.saveAndFlush(note);

        assertTrue(notesRepository.existsById(persistedNote.getId()));
    }
}