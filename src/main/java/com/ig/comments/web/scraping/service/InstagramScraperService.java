package com.ig.comments.web.scraping.service;

import com.ig.comments.web.scraping.component.InstagramBrowserClient;
import com.ig.comments.web.scraping.dto.InstagramPostDataDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InstagramScraperService {

    private static final Logger log = LoggerFactory.getLogger(InstagramScraperService.class);

    private final InstagramBrowserClient browserClient;
    private final OpenRouterAiService aiService;

    public InstagramScraperService(InstagramBrowserClient browserClient, OpenRouterAiService aiService) {
        this.browserClient = browserClient;
        this.aiService = aiService;
    }

    public String scrapePost(String url) {
        log.info("starting scraping for url: {}", url);
        InstagramPostDataDTO igPostData = browserClient.extractPostData(url);
        return aiService.analyzeInstagramPost(igPostData);
    }
}
