package com.notesapp.statistic.kafka;

import com.notesapp.model.event.NoteAccessEvent;
import com.notesapp.statistic.model.persistence.NoteAccessEventPo;
import com.notesapp.statistic.repository.NotesAccessEventsRepository;
import org.springframework.stereotype.Service;

@Service
public class NotesAccessEventsService {

    private final NotesAccessEventsRepository notesAccessEventsRepository;

    public NotesAccessEventsService(NotesAccessEventsRepository notesAccessEventsRepository) {
        this.notesAccessEventsRepository = notesAccessEventsRepository;
    }

    public void onEvent(NoteAccessEvent event) {
        NoteAccessEventPo noteAccessEventPo = convertEvent(event);
        notesAccessEventsRepository.save(noteAccessEventPo);
    }

    private static NoteAccessEventPo convertEvent(NoteAccessEvent event) {
        return new NoteAccessEventPo(event.note().id(), event.event());
    }
}
