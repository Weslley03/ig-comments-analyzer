package com.ig.comments.web.scraping.service;

import com.ig.comments.web.scraping.component.InstagramBrowserClient;
import com.ig.comments.web.scraping.dto.InstagramPostDataDTO;
import com.ig.comments.web.scraping.exception.InsufficientCreditsException;
import com.ig.comments.web.scraping.exception.ResourceNotFoundException;
import com.ig.comments.web.scraping.model.CommentAnalysis;
import com.ig.comments.web.scraping.model.CommentAnalysisHistory;
import com.ig.comments.web.scraping.model.Permission;
import com.ig.comments.web.scraping.model.User;
import com.ig.comments.web.scraping.repository.CommentAnalysisHistoryRepository;
import com.ig.comments.web.scraping.repository.CommentAnalysisRepository;
import com.ig.comments.web.scraping.repository.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstagramScraperService {

    private static final Logger log = LoggerFactory.getLogger(InstagramScraperService.class);

    private final InstagramBrowserClient browserClient;
    private final OpenRouterAiService aiService;
    private final CommentAnalysisRepository commentAnalysisRepository;
    private final CommentAnalysisHistoryRepository commentAnalysisHistoryRepository;
    private final PermissionRepository permissionRepository;

    public InstagramScraperService(
            InstagramBrowserClient browserClient,
            OpenRouterAiService aiService,
            CommentAnalysisRepository commentAnalysisRepository,
            CommentAnalysisHistoryRepository commentAnalysisHistoryRepository,
            PermissionRepository permissionRepository) {

        this.browserClient = browserClient;
        this.aiService = aiService;
        this.commentAnalysisRepository = commentAnalysisRepository;
        this.commentAnalysisHistoryRepository = commentAnalysisHistoryRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public String scrapePost(String url) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        Permission permission = permissionRepository.findByUserId(currentUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException("permissão não encontrada para o usuário."));

        if (permission.getRemainingLimit() <= 0) {
            throw new InsufficientCreditsException("créditos insuficientes para realizar esta operação.");
        }

        log.info("starting scraping for url: {}", url);
        InstagramPostDataDTO igPostData = browserClient.extractPostData(url, permission.getCommentsPerQuery());
        String result = aiService.analyzeInstagramPost(igPostData);

        CommentAnalysis analysis = new CommentAnalysis();
        analysis.setIgUrl(url);
        analysis.setDescriptionContent(igPostData.description());
        analysis.setResult(result);
        analysis = commentAnalysisRepository.save(analysis);

        String normalizedUrl = url.replaceAll("/$", "");
        String label = normalizedUrl.substring(Math.max(0, normalizedUrl.length() - 8));
        String verdict = extractVerdict(result);

        CommentAnalysisHistory history = new CommentAnalysisHistory();
        history.setAnalysis(analysis);
        history.setUser(currentUser);
        history.setLabel(label);
        history.setVerdict(verdict);
        commentAnalysisHistoryRepository.save(history);

        permission.setRemainingLimit(permission.getRemainingLimit() - 1);
        permissionRepository.save(permission);

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
