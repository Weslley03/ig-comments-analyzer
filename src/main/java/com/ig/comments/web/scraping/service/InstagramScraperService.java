package com.ig.comments.web.scraping.service;

import com.ig.comments.web.scraping.component.InstagramBrowserClient;
import com.ig.comments.web.scraping.dto.InstagramPostDataDTO;
import com.ig.comments.web.scraping.model.CommentAnalysis;
import com.ig.comments.web.scraping.model.CommentAnalysisHistory;
import com.ig.comments.web.scraping.model.User;
import com.ig.comments.web.scraping.repository.CommentAnalysisHistoryRepository;
import com.ig.comments.web.scraping.repository.CommentAnalysisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class InstagramScraperService {

    private static final Logger log = LoggerFactory.getLogger(InstagramScraperService.class);

    private final InstagramBrowserClient browserClient;
    private final OpenRouterAiService aiService;
    private final CommentAnalysisRepository commentAnalysisRepository;
    private final CommentAnalysisHistoryRepository commentAnalysisHistoryRepository;

    public InstagramScraperService(
            InstagramBrowserClient browserClient,
            OpenRouterAiService aiService,
            CommentAnalysisRepository commentAnalysisRepository,
            CommentAnalysisHistoryRepository commentAnalysisHistoryRepository) {

        this.browserClient = browserClient;
        this.aiService = aiService;
        this.commentAnalysisRepository = commentAnalysisRepository;
        this.commentAnalysisHistoryRepository = commentAnalysisHistoryRepository;
    }

    public String scrapePost(String url) {
        log.info("starting scraping for url: {}", url);
        InstagramPostDataDTO igPostData = browserClient.extractPostData(url);
        String result = aiService.analyzeInstagramPost(igPostData);

        CommentAnalysis analysis = new CommentAnalysis();
        analysis.setIgUrl(url);
        analysis.setDescriptionContent(igPostData.description());
        analysis.setResult(result);
        analysis = commentAnalysisRepository.save(analysis);

        String normalizedUrl = url.replaceAll("/$", "");
        String label = normalizedUrl.substring(Math.max(0, normalizedUrl.length() - 8));
        String verdict = extractVerdict(result);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof User user) {
            CommentAnalysisHistory history = new CommentAnalysisHistory();
            history.setAnalysis(analysis);
            history.setUser(user);
            history.setLabel(label);
            history.setVerdict(verdict);
            commentAnalysisHistoryRepository.save(history);
        }

        return result;
    }

    private String extractVerdict(String result) {
        String firstLine = result.lines().findFirst().orElse("").toLowerCase();
        if (firstLine.contains("positivo")) return "pos";
        if (firstLine.contains("negativo")) return "neg";
        if (firstLine.contains("misto")) return "misto";
        if (firstLine.contains("neutro")) return "neutro";
        return "neutro";
    }
}
