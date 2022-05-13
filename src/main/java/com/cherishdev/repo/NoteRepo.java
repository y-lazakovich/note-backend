package com.cherishdev.repo;

import com.cherishdev.domain.Note;
import com.cherishdev.enumeration.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {

    List<Note> findByLevel(Level level);

    void deleteById(Long id);

}
