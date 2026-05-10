package com.ig.comments.web.scraping.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ig.comments.web.scraping.dto.CommentAnalysisHistoryResponseDTO;
import com.ig.comments.web.scraping.repository.CommentAnalysisHistoryRepository;

@Service
public class CommentAnalysisHistoryService {

    private final CommentAnalysisHistoryRepository commentAnalysisHistoryRepository;

    public CommentAnalysisHistoryService(CommentAnalysisHistoryRepository commentAnalysisHistoryRepository) {
        this.commentAnalysisHistoryRepository = commentAnalysisHistoryRepository;
    }

    public List<CommentAnalysisHistoryResponseDTO> listByUser(UUID userId) {
        return commentAnalysisHistoryRepository.findByUserId(userId).stream()
            .map(history -> new CommentAnalysisHistoryResponseDTO(
                    history.getId(),
                    history.getAnalysis().getId(),
                    history.getUser().getId(),
                    history.getLabel(),
                    history.getVerdict(),
                    history.getCreatedAt(),
                    history.getUpdatedAt()))
            .toList();
    }
}
