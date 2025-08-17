package com.example.Enotes_API_Service.repository;

import com.example.Enotes_API_Service.entity.FavoriteNotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteNotesRepository extends JpaRepository<FavoriteNotes, Integer> {
    List<FavoriteNotes> findByUserId(int userId);
}
