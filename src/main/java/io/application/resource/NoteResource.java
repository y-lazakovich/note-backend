package io.application.resource;

import io.application.domain.HttpResponse;
import io.application.domain.Note;
import io.application.enumeration.Level;
import io.application.service.NoteService;
import io.application.util.DateUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(path = "/note")
public class NoteResource {
    private final NoteService noteService;

    public NoteResource(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/all")
    public ResponseEntity<HttpResponse<Note>> getNotes() {
        return ResponseEntity.ok().body(noteService.getNotes());
    }

    @PostMapping("/add")
    public ResponseEntity<HttpResponse<Note>> saveNote(@RequestBody @Valid Note note) {
        return ResponseEntity.created(
                URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/note/all").toUriString())
        ).body(noteService.saveNote(note));
    }

    @GetMapping("/filter")
    public ResponseEntity<HttpResponse<Note>> filterNotes(@RequestParam(value = "level") Level level) {
        return ResponseEntity.ok().body(noteService.filterNotes(level));
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse<Note>> updateNote(@RequestBody @Valid Note note) {
        return ResponseEntity.ok().body(noteService.updateNote(note));
    }

    @DeleteMapping("/delete/{noteId}")
    public ResponseEntity<HttpResponse<Note>> updateNote(@PathVariable(value = "noteId") Long id) {
        return ResponseEntity.ok().body(noteService.deleteNote(id));
    }

    @RequestMapping("/error")
    public ResponseEntity<HttpResponse<?>> handleError(HttpServletRequest request) {
        return new ResponseEntity<>(new HttpResponse()
                .setReason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                .setDeveloperMessage("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                .setStatus(NOT_FOUND)
                .setStatusCode(NOT_FOUND.value())
                .setTimestamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter())), NOT_FOUND);
    }
}
