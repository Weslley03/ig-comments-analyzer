package com.ig.comments.web.scraping.repository;

import com.ig.comments.web.scraping.model.CommentAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentAnalysisRepository extends JpaRepository<CommentAnalysis, Long> {
}
