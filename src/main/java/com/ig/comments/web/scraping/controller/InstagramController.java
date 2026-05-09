package com.ig.comments.web.scraping.controller;

import com.ig.comments.web.scraping.dto.InstagramRequestDTO;
import com.ig.comments.web.scraping.dto.ResponseApiDTO;
import com.ig.comments.web.scraping.service.InstagramScraperService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instagram")
public class InstagramController {

    private final InstagramScraperService service;

    public InstagramController(InstagramScraperService service) {
        this.service = service;
    }

    @GetMapping("/comments")
    public ResponseEntity<ResponseApiDTO<String>> getAnalysis(@ModelAttribute @Valid InstagramRequestDTO request) {
        String analysis = service.scrapePost(request.getUrl());
        return ResponseEntity.ok(new ResponseApiDTO<>(
                HttpStatus.OK.value(),
                "análise concluída com sucesso.",
                analysis
        ));
    }
}
