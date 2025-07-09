package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.dto.NotesDto;
import com.example.Enotes_API_Service.entity.FileDetails;
import com.example.Enotes_API_Service.entity.Notes;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.repository.CategoryRepository;
import com.example.Enotes_API_Service.repository.FileRepository;
import com.example.Enotes_API_Service.repository.NotesRepository;
import com.example.Enotes_API_Service.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class NotesServiceImpl implements NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public Boolean saveNotes(String notes, MultipartFile file) throws Exception {

        ObjectMapper ob = new ObjectMapper();
        NotesDto notesDto = ob.readValue(notes, NotesDto.class);

       // check category id
        checkCategoryExist(notesDto.getCategory());

        Notes notesMap = mapper.map(notesDto, Notes.class);

        FileDetails fileDetails = saveFileDetails(file);

        if(!ObjectUtils.isEmpty(fileDetails)){
            notesMap.setFileDetails(fileDetails);
        }else {
            notesMap.setFileDetails(null);
        }

        Notes saveNotes = notesRepository.save(notesMap);
        if(!ObjectUtils.isEmpty(saveNotes)) {
            return true;
        }
        return false;
    }

    private FileDetails saveFileDetails(MultipartFile file) throws Exception {

        if(!ObjectUtils.isEmpty(file) && !file.isEmpty()){

            String originalFileName = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFileName);

            List<String> extensionsAllow = Arrays.asList("pdf", "xlsx", "jpg", "png");
            if(!extensionsAllow.contains(extension)){
                throw new IllegalArgumentException("invalid file format! Supported formats are pdf, xlsx, jpg and png.");
            }

            FileDetails fileDetails = new FileDetails();

            fileDetails.setOriginalFileName(originalFileName);
            fileDetails.setDisplayFileName((getDisplayName(originalFileName)));

            String rndString = UUID.randomUUID().toString();
            String uploadFileName = rndString+"."+extension;
            fileDetails.setUploadFileName(uploadFileName);

            fileDetails.setFileSize(file.getSize());

            File saveFile = new File(uploadPath);
            if(!saveFile.exists()) saveFile.mkdir();
            String storePath = uploadPath.concat(uploadFileName);

            fileDetails.setPath(storePath);

            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
            if(upload!=0){
                return fileRepository.save(fileDetails);
            }
        }

        return null;
    }

    private String getDisplayName(String originalFileName) {

        String extension = FilenameUtils.getExtension(originalFileName);
        String fileName = FilenameUtils.removeExtension(originalFileName);

        if(fileName.length() > 8){
            fileName = fileName.substring(0, 7);
        }
        fileName = fileName+"."+extension;
        return fileName;
    }

    private void checkCategoryExist(NotesDto.CategoryDto category) throws ResourceNotFoundException {
        categoryRepository.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("Category Id does not exist!"));
    }

    @Override
    public List<NotesDto> getAllNotes() {
        return notesRepository.findAll().stream()
                .map(note->mapper.map(note, NotesDto.class)).toList();
    }

    @Override
    public FileDetails getFileDetails(Integer id) throws Exception {
        return fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("file is not available!"));
    }

    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws Exception {

        InputStream io = new FileInputStream(fileDetails.getPath());
        return StreamUtils.copyToByteArray(io);
    }
}
