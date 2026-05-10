package com.ig.comments.web.scraping.service;

import org.springframework.stereotype.Service;

import com.ig.comments.web.scraping.dto.CommentAnalysisResponseDTO;
import com.ig.comments.web.scraping.model.CommentAnalysis;
import com.ig.comments.web.scraping.repository.CommentAnalysisRepository;

@Service
public class CommentAnalysisService {

    private final CommentAnalysisRepository commentAnalysisRepository;

    public CommentAnalysisService(CommentAnalysisRepository commentAnalysisRepository) {
        this.commentAnalysisRepository = commentAnalysisRepository;
    }

    public CommentAnalysisResponseDTO findById(Long analysisId) {
        CommentAnalysis commentAnalysis = this.commentAnalysisRepository
            .findById(analysisId)
            .orElseThrow(() -> new RuntimeException("Análise não encontrada."));

        return new CommentAnalysisResponseDTO(
                commentAnalysis.getId(),
                commentAnalysis.getIgUrl(),
                commentAnalysis.getDescriptionContent(),
                commentAnalysis.getResult(),
                commentAnalysis.getMetadata(),
                commentAnalysis.getCreatedAt());
    }
}

