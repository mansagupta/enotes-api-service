package com.example.Enotes_API_Service.controller;


import com.example.Enotes_API_Service.dto.NotesDto;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.service.NotesService;
import com.example.Enotes_API_Service.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @PostMapping("/save")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws ResourceNotFoundException, IOException {
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
}
