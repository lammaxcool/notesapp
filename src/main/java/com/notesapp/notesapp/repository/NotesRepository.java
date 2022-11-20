package com.notesapp.notesapp.repository;

import com.notesapp.notesapp.model.persistence.NotePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<NotePo, Long> {
}