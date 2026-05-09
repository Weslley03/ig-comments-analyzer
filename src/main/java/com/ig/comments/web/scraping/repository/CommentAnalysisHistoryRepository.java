package com.ig.comments.web.scraping.repository;

import com.ig.comments.web.scraping.model.CommentAnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentAnalysisHistoryRepository extends JpaRepository<CommentAnalysisHistory, Long> {
    List<CommentAnalysisHistory> findByUserId(UUID userId);
}
