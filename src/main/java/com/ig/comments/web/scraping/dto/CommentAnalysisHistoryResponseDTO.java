package com.ig.comments.web.scraping.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CommentAnalysisHistoryResponseDTO(
        Long id,
        Long analysisId,
        UUID userId,
        String label,
        String verdict,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {}
