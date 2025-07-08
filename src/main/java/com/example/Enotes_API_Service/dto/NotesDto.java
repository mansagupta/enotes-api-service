package com.example.Enotes_API_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesDto {

    private Integer id;

    private String title;

    private String description;

    private CategoryDto category;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    private FileDto fileDetails;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileDto{
        private Integer id;

        private String originalFileName;

        private String displayFileName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDto{
        private Integer id;

        private String name;
    }
}
