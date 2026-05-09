package com.ig.comments.web.scraping.controller;

import com.ig.comments.web.scraping.dto.AssignPlanRequestDTO;
import com.ig.comments.web.scraping.dto.UserPlanResponseDTO;
import com.ig.comments.web.scraping.model.User;
import com.ig.comments.web.scraping.service.UserPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-plans")
public class UserPlanController {

    private final UserPlanService userPlanService;

    public UserPlanController(UserPlanService userPlanService) {
        this.userPlanService = userPlanService;
    }

    @PostMapping
    public ResponseEntity<UserPlanResponseDTO> assign(@AuthenticationPrincipal User user, @Valid @RequestBody AssignPlanRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userPlanService.assign(user.getId(), dto));
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserPlanResponseDTO>> listMyPlans(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userPlanService.listByUser(user.getId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Void> getByUserId(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
