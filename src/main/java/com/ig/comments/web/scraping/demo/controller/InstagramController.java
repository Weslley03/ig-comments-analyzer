package com.ig.comments.web.scraping.demo.controller;

import com.ig.comments.web.scraping.demo.dto.ResponseApiDTO;
import com.ig.comments.web.scraping.demo.exception.InvalidUrlException;
import com.ig.comments.web.scraping.demo.service.InstagramScraperService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instagram")
public class InstagramController {

    private final InstagramScraperService service;

    public InstagramController(InstagramScraperService service) {
        this.service = service;
    }

    @GetMapping("/comments")
    public ResponseEntity<ResponseApiDTO<String>> getAnalysis(@RequestParam String url) {
        if (url.isBlank()) {
            throw new InvalidUrlException("a url não pode estar vazia.");
        }
        if (!url.contains("instagram.com")) {
            throw new InvalidUrlException("a url deve ser um link válido do instagram.");
        }

        String analysis = service.scrapePost(url);
        return ResponseEntity.ok(new ResponseApiDTO<>(
                HttpStatus.OK.value(),
                "análise concluída com sucesso.",
                analysis
        ));
    }
}
