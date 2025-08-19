package com.example.Enotes_API_Service.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotesRequest {

    private String title;

    private String description;

    private NotesDto.CategoryDto category;

}
