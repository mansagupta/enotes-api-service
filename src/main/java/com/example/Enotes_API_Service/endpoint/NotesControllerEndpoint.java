package com.example.Enotes_API_Service.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/notes")
public interface NotesControllerEndpoint {

    @PostMapping("/save")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception;

    @GetMapping("/get")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> getAllNotes();

    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @GetMapping("/search")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> searchNotes(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;

    @GetMapping("/restore/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;

    @GetMapping("/recycle-bin")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getUserRecycleBinNotes();

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;

    @DeleteMapping("/empty-bin")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> emptyUserRecycleBin();

    @GetMapping("/fav/{notesId}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> favoriteNotes(@PathVariable Integer notesId) throws Exception;

    @DeleteMapping("/unFav/{favNotesId}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> unFavoriteNotes(@PathVariable Integer favNotesId) throws Exception;

    @GetMapping("/fav-notes")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getUserFavoriteNotes() throws Exception;

    @GetMapping("/copy/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;
}
