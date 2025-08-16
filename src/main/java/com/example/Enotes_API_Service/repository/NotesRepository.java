package com.example.Enotes_API_Service.repository;

import com.example.Enotes_API_Service.entity.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotesRepository extends JpaRepository<Notes, Integer> {
    List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);

    Page<Notes> findByCreatedByAndIsDeletedFalse(Integer userId, Pageable pageable);

    List<Notes> findAllByIsDeletedAndDeletedOnBefore(boolean b, LocalDateTime cutOffDate);

    @Query("select n from Notes n where (Lower(n.title) like lower(concat('%', :keyword, '%')) "
            + "or Lower(n.description) like lower(concat('%', :keyword, '%')) "
            + "or Lower(n.category.name) like lower(concat('%', :keyword, '%'))) "
            + "and n.isDeleted = false "
            + "and n.createdBy = :userId")
    Page<Notes> searchNotes(@Param("keyword") String keyword, @Param("userId") Integer userId, Pageable pageable);
}
