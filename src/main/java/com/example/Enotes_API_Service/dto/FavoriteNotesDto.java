package com.example.Enotes_API_Service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FavoriteNotesDto {
    private Integer id;

    private NotesDto notesId;

    private Integer userId;
}
