package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.FavoriteNotesDto;
import com.example.Enotes_API_Service.dto.NotesDto;
import com.example.Enotes_API_Service.dto.NotesResponse;
import com.example.Enotes_API_Service.endpoint.NotesControllerEndpoint;
import com.example.Enotes_API_Service.entity.FileDetails;
import com.example.Enotes_API_Service.service.NotesService;
import com.example.Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
public class NotesController implements NotesControllerEndpoint {

    @Autowired
    private NotesService notesService;

    @Override
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception {
        Boolean saveNotes = notesService.saveNotes(notes, file);
        if(saveNotes) {
            return CommonUtil.createBuildResponseMessage("notes saved success", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getAllNotes() {
        List<NotesDto> notes = notesService.getAllNotes();
        if(CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        NotesResponse notes = notesService.getAllNotesByUser(pageNo, pageSize);
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchNotes(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        NotesResponse notes = notesService.getAllNotesByUserSearch(key, pageNo, pageSize);
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {
        FileDetails fileDetails = notesService.getFileDetails(id);
        byte[] data= notesService.downloadFile(fileDetails);

        HttpHeaders headers = new HttpHeaders();
        String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());
        return ResponseEntity.ok().headers(headers).body(data);

    }

    @Override
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Restore Success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserRecycleBinNotes() {
        List<NotesDto> notes = notesService.getUserRecycleBinNotes();
        if(notes.isEmpty()){
            return CommonUtil.createBuildResponseMessage("Notes not available in bin!", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> emptyUserRecycleBin() {
        notesService.emptyRecycleBin();
        return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> favoriteNotes(@PathVariable Integer notesId) throws Exception {
        notesService.favoriteNotes(notesId);
        return CommonUtil.createBuildResponseMessage("Favorite notes added.", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> unFavoriteNotes(@PathVariable Integer favNotesId) throws Exception {
        notesService.unFavoriteNotes(favNotesId);
        return CommonUtil.createBuildResponseMessage("Favorite notes removed.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserFavoriteNotes() throws Exception {
        List<FavoriteNotesDto> userFavoriteNotes = notesService.getUserFavoriteNotes();
        if(CollectionUtils.isEmpty((userFavoriteNotes))){
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception {
        Boolean copyNotes = notesService.copyNotes(id);
        if(copyNotes) {
            return CommonUtil.createBuildResponseMessage("Notes copied.", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Copy failed! Try again.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
