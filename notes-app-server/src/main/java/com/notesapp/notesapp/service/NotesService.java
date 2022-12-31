package com.notesapp.notesapp.service;

import com.notesapp.notesapp.model.persistence.NotePo;
import com.notesapp.notesapp.model.view.NoteView;
import com.notesapp.notesapp.repository.NotesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotesService.class);

    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public NoteView createNote(NoteView noteView) {
        var notePo = convertNote(noteView);
        var persistedNote = notesRepository.save(notePo);

        LOGGER.info("Created new entity: {}", notePo);

        return new NoteView(persistedNote);
    }

    public List<NoteView> getAllNotes() {
        return notesRepository.findAll().stream()
                .map(NoteView::new)
                .toList();
    }

    public NoteView getNoteById(Long id) {
        var notePo = notesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Entity does not exists!"));
        return new NoteView(notePo);
    }

    public NoteView updateNote(NoteView noteView) {
        var notePoToUpdate = convertNote(noteView);
        if (notesRepository.existsById(noteView.id())) {
            var updatedNote = notesRepository.save(notePoToUpdate);
            LOGGER.info("Successfully updated entity: {} -> {}", notePoToUpdate, updatedNote);
            return new NoteView(updatedNote);
        }

        throw new IllegalArgumentException("Entity does not exists!");
    }

    public void deleteNoteById(Long id) {
        if (!notesRepository.existsById(id)) {
            throw new IllegalArgumentException("Entity does not exists!");
        }

        notesRepository.deleteById(id);
        LOGGER.info("Successfully deleted entity 'Note' with id: {}", id);
    }

    private static NotePo convertNote(NoteView noteView) {
        return new NotePo(noteView.id(), noteView.title(), noteView.content());
    }
}