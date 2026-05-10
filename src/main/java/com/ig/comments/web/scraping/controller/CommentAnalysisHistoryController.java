package com.ig.comments.web.scraping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ig.comments.web.scraping.dto.CommentAnalysisHistoryResponseDTO;
import com.ig.comments.web.scraping.dto.ResponseApiDTO;
import com.ig.comments.web.scraping.model.User;
import com.ig.comments.web.scraping.service.CommentAnalysisHistoryService;

@RestController
@RequestMapping("/analysis-history")
public class CommentAnalysisHistoryController {

    private final CommentAnalysisHistoryService commentAnalysisHistoryService;

    public CommentAnalysisHistoryController(CommentAnalysisHistoryService commentAnalysisHistoryService) {
        this.commentAnalysisHistoryService = commentAnalysisHistoryService;
    }

    @GetMapping()
    public ResponseEntity<ResponseApiDTO<List<CommentAnalysisHistoryResponseDTO>>> listMyHistory(@AuthenticationPrincipal User user) {
        List<CommentAnalysisHistoryResponseDTO> commentAnalysisHistoryResponseDTO = commentAnalysisHistoryService.listByUser(user.getId());
        return ResponseEntity.ok(new ResponseApiDTO<List<CommentAnalysisHistoryResponseDTO>>(
            HttpStatus.OK.value(),
            "histórico do usuário recuperado.",
            commentAnalysisHistoryResponseDTO
        ));
    }
}
