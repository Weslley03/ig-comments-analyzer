package com.ig.comments.web.scraping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ig.comments.web.scraping.dto.CommentAnalysisResponseDTO;
import com.ig.comments.web.scraping.dto.ResponseApiDTO;
import com.ig.comments.web.scraping.service.CommentAnalysisService;

@RestController
@RequestMapping("/analysis-comment")
public class CommentAnalysisController {
    private final CommentAnalysisService commentAnalysisService;

    public CommentAnalysisController(CommentAnalysisService commentAnalysisService) {
        this.commentAnalysisService = commentAnalysisService;
    }

    @GetMapping
    public ResponseEntity<ResponseApiDTO<CommentAnalysisResponseDTO>> findById(@RequestParam Long analysisId) {
        CommentAnalysisResponseDTO response = commentAnalysisService.findById(analysisId);
        return ResponseEntity.ok(new ResponseApiDTO<CommentAnalysisResponseDTO>(
            HttpStatus.OK.value(),
            "análise recuperada com sucesso.",
            response
        ));
    }
}
