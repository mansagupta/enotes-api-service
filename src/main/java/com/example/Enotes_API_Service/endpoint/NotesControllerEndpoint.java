package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.NotesDto;
import com.example.Enotes_API_Service.dto.NotesRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Notes", description = "APIs for managing notes")
@RequestMapping("/api/v1/notes")
public interface NotesControllerEndpoint {

    @Operation(summary = "Save notes", tags = {"Notes", "User"}, description = "Add new notes")
    @PostMapping(value = "/save", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> saveNotes(@RequestParam @Parameter(description = "Json String notes", required = true,
            content = @Content(schema = @Schema(implementation = NotesRequest.class))) String notes,
                                @RequestParam(required = false) MultipartFile file) throws Exception;

    @Operation(summary = "Get all notes", tags = {"Notes", "Admin"}, description = "Retrieve all the notes available")
    @GetMapping("/get")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> getAllNotes();

    @Operation(summary = "Get all the user notes", tags = {"Notes", "User"}, description = "Retrieve all the notes of a user")
    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @Operation(summary = "Search notes", tags = {"Notes", "User"}, description = "Search notes using any keyword")
    @GetMapping("/search")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> searchNotes(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @Operation(summary = "Download notes file", tags = {"Notes", "Admin", "User"}, description = "Download the uploaded notes file")
    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Delete notes", tags = {"Notes", "User"}, description = "Delete user notes")
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Restore notes", tags = {"Notes", "User"}, description = "Restore all the deleted notes")
    @GetMapping("/restore/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Get notes from recycle bin", tags = {"Notes", "User"}, description = "Retrieve recycle bin notes of a user")
    @GetMapping("/recycle-bin")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getUserRecycleBinNotes();

    @Operation(summary = "Hard delete notes", tags = {"Notes", "User"}, description = "Permanently delete notes")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Empty recycle bin", tags = {"Notes", "User"}, description = "Empty user recycle bin")
    @DeleteMapping("/empty-bin")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> emptyUserRecycleBin();

    @Operation(summary = "Favorite notes", tags = {"Notes", "User"}, description = "Make notes favorite")
    @GetMapping("/fav/{notesId}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> favoriteNotes(@PathVariable Integer notesId) throws Exception;

    @Operation(summary = "Un-favorite notes", tags = {"Notes", "User"}, description = "Make notes un-favorite")
    @DeleteMapping("/unFav/{favNotesId}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> unFavoriteNotes(@PathVariable Integer favNotesId) throws Exception;

    @Operation(summary = "Get favorite notes", tags = {"Notes", "User"}, description = "Retrieve user favorite notes")
    @GetMapping("/fav-notes")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getUserFavoriteNotes() throws Exception;

    @Operation(summary = "Copy notes", tags = {"Notes", "User"}, description = "Copy user notes")
    @GetMapping("/copy/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;
}
