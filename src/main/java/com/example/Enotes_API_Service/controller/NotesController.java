package com.example.Enotes_API_Service.controller;


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
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception {
        Boolean saveNotes = notesService.saveNotes(notes, file);
        if(saveNotes) {
            return CommonUtil.createBuildResponseMessage("notes saved success", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllNotes(){
        List<NotesDto> notes = notesService.getAllNotes();
        if(CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Integer userId = 1;
        NotesResponse notes = notesService.getAllNotesByUser(userId, pageNo, pageSize);
//        if(CollectionUtils.isEmpty(notes)) {
//            return ResponseEntity.noContent().build();
//        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
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
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Restore Success", HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
        Integer userId = 1;
        List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
        if(notes.isEmpty()){
            return CommonUtil.createBuildResponseMessage("Notes not available in bin!", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
    }

    @DeleteMapping("/empty-bin")
    public ResponseEntity<?> emptyRecycleBin() throws Exception {
        int userId = 1;
        notesService.emptyRecycleBin(userId);
        return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
    }
}
