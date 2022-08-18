package io.application.repo;

import io.application.domain.Note;
import io.application.enumeration.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {

    List<Note> findByLevel(Level level);

    void deleteById(Long id);

}
