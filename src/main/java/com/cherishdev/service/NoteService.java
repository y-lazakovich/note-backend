package com.cherishdev.service;

import com.cherishdev.domain.HttpResponse;
import com.cherishdev.domain.Note;
import com.cherishdev.enumeration.Level;
import com.cherishdev.exception.NoteNotFoundException;
import com.cherishdev.repo.NoteRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.cherishdev.util.DateUtil.dateTimeFormatter;
import static java.util.Collections.singleton;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class NoteService {
    private static final Logger LOG = LoggerFactory.getLogger(NoteService.class);

    private final NoteRepo noteRepo;

    public NoteService(NoteRepo noteRepo) {
        this.noteRepo = noteRepo;
    }

    public HttpResponse<Note> getNotes() {
        LOG.info("Fetching all notes from database");
        return new HttpResponse<Note>()
                .setData(noteRepo.findAll())
                .setMessage(noteRepo.count() > 0 ? noteRepo.count() + " notes retrieved" : "No notes to display")
                .setStatus(OK)
                .setStatusCode(OK.value())
                .setTimestamp(LocalDateTime.now().format(dateTimeFormatter()));
    }

    public HttpResponse<Note> filterNotes(Level level) {
        List<Note> notes = noteRepo.findByLevel(level);
        LOG.info("Fetching all the notes by level {}", level);
        return new HttpResponse<Note>()
                .setData(notes)
                .setMessage(notes.size() + " notes are of " + level + " importance")
                .setStatus(OK)
                .setStatusCode(OK.value())
                .setTimestamp(LocalDateTime.now().format(dateTimeFormatter()));
    }

    public HttpResponse<Note> saveNote(Note note) {
        LOG.info("Saving new note to the database");
        note.setCreatedAt(LocalDateTime.now());
        return new HttpResponse<Note>()
                .setData(singleton(noteRepo.save(note)))
                .setMessage("Note created successfully")
                .setStatus(CREATED)
                .setStatusCode(CREATED.value())
                .setTimestamp(LocalDateTime.now().format(dateTimeFormatter()));
    }

    public HttpResponse<Note> updateNote(Note note) throws NoteNotFoundException {
        LOG.info("Updating note to the database");
        Optional<Note> optionalNote = Optional.ofNullable(noteRepo.findById(note.getId()))
                .orElseThrow(() -> new NoteNotFoundException("The note was not found on the server"));
        Note updatedNote = optionalNote.get()
                .setId(note.getId())
                .setTitle(note.getTitle())
                .setDescription(note.getDescription())
                .setLevel(note.getLevel())
                .setCreatedAt(LocalDateTime.now());
        noteRepo.save(updatedNote);
        return new HttpResponse<Note>()
                .setData(singleton(noteRepo.save(note)))
                .setMessage("Note updated successfully")
                .setStatus(OK)
                .setStatusCode(OK.value())
                .setTimestamp(LocalDateTime.now().format(dateTimeFormatter()));
    }

    public HttpResponse<Note> deleteNote(Long id) throws NoteNotFoundException {
        LOG.info("Deleting note from the database by id {}", id);
        Optional<Note> optionalNote = Optional.ofNullable(noteRepo.findById(id))
                .orElseThrow(() -> new NoteNotFoundException("The note was not found on the server"));
        optionalNote.ifPresent(noteRepo::delete);
        return new HttpResponse<Note>()
                .setData(singleton(optionalNote.get()))
                .setMessage("Note deleted successfully")
                .setStatus(OK)
                .setStatusCode(OK.value())
                .setTimestamp(LocalDateTime.now().format(dateTimeFormatter()));
    }
}
