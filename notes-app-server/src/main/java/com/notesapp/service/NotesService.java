package com.notesapp.service;

import com.notesapp.model.persistence.NotePo;
import com.notesapp.model.view.NoteView;
import com.notesapp.repository.NotesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotesService.class);

    private final NotesRepository notesRepository;
    private final NotificationService<NoteView> notificationService;

    public NotesService(NotesRepository notesRepository, NotificationService<NoteView> notificationService) {
        this.notesRepository = notesRepository;
        this.notificationService = notificationService;
    }

    public NoteView createNote(NoteView noteView) {
        var notePo = convertNote(noteView);
        var persistedNote = notesRepository.save(notePo);

        var createdNote = new NoteView(persistedNote);
        notificationService.onCreate(createdNote);
        LOGGER.info("Created new entity: {}", notePo);

        return createdNote;
    }

    public List<NoteView> getAllNotes() {
        List<NoteView> notes = notesRepository.findAll().stream()
                .map(NoteView::new)
                .toList();

        notes.forEach(notificationService::onRead);
        return notes;
    }

    public NoteView getNoteById(Long id) {
        var notePo = notesRepository.findById(id).orElseThrow();
        var note = new NoteView(notePo);

        notificationService.onRead(note);
        return note;
    }

    public NoteView updateNote(NoteView noteView) {
        var notePoToUpdate = convertNote(noteView);
        if (notesRepository.existsById(noteView.id())) {
            var updatedNote = notesRepository.save(notePoToUpdate);
            LOGGER.info("Successfully updated entity: {} -> {}", notePoToUpdate, updatedNote);
            var note = new NoteView(updatedNote);

            notificationService.onUpdate(note);
            return note;
        }

        throw new IllegalArgumentException("Entity does not exists!");
    }

    public void deleteNoteById(Long id) {
        var notePo = notesRepository.findById(id).orElseThrow();
        notesRepository.deleteById(id);
        notificationService.onDelete(new NoteView(notePo));
        LOGGER.info("Successfully deleted entity 'Note' with id: {}", id);
    }

    private static NotePo convertNote(NoteView noteView) {
        return new NotePo(noteView.id(), noteView.title(), noteView.content());
    }
}