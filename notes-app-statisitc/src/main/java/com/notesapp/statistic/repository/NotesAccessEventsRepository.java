package com.notesapp.statistic.repository;

import com.notesapp.statistic.model.persistence.NoteAccessEventPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesAccessEventsRepository extends JpaRepository<NoteAccessEventPo, Long> {
}