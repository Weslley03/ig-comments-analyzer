package com.ig.comments.web.scraping.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserPlanResponseDTO(
        Long id,
        UUID userId,
        Long planId,
        String planDescription,
        String planRole,
        OffsetDateTime createdAt
) {}
