package com.example.Enotes_API_Service.controller;


import com.example.Enotes_API_Service.dto.FavoriteNotesDto;
import com.example.Enotes_API_Service.dto.NotesDto;
import com.example.Enotes_API_Service.dto.NotesResponse;
import com.example.Enotes_API_Service.entity.FileDetails;
import com.example.Enotes_API_Service.service.NotesService;
import com.example.Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception {
        Boolean saveNotes = notesService.saveNotes(notes, file);
        if(saveNotes) {
            return CommonUtil.createBuildResponseMessage("notes saved success", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> getAllNotes(){
        List<NotesDto> notes = notesService.getAllNotes();
        if(CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Integer userId = 1;
        NotesResponse notes = notesService.getAllNotesByUser(userId, pageNo, pageSize);
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {
        FileDetails fileDetails = notesService.getFileDetails(id);
        byte[] data= notesService.downloadFile(fileDetails);

        HttpHeaders headers = new HttpHeaders();
        String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());
        return ResponseEntity.ok().headers(headers).body(data);

    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Restore Success", HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
        Integer userId = 1;
        List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
        if(notes.isEmpty()){
            return CommonUtil.createBuildResponseMessage("Notes not available in bin!", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
    }

    @DeleteMapping("/empty-bin")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> emptyRecycleBin() throws Exception {
        int userId = 1;
        notesService.emptyRecycleBin(userId);
        return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
    }

    @GetMapping("/fav/{notesId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> favoriteNotes(@PathVariable Integer notesId) throws Exception {
        notesService.favoriteNotes(notesId);
        return CommonUtil.createBuildResponseMessage("Favorite notes added.", HttpStatus.CREATED);
    }

    @DeleteMapping("/unFav/{favNotesId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> unFavoriteNotes(@PathVariable Integer favNotesId) throws Exception {
        notesService.unFavoriteNotes(favNotesId);
        return CommonUtil.createBuildResponseMessage("Favorite notes removed.", HttpStatus.OK);
    }

    @GetMapping("/fav-notes")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> getUserFavoriteNotes() throws Exception {
        List<FavoriteNotesDto> userFavoriteNotes = notesService.getUserFavoriteNotes();
        if(CollectionUtils.isEmpty((userFavoriteNotes))){
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
    }

    @GetMapping("/copy/{id}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception {
        Boolean copyNotes = notesService.copyNotes(id);
        if(copyNotes) {
            return CommonUtil.createBuildResponseMessage("Notes copied.", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Copy failed! Try again.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
