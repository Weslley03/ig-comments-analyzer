package com.ig.comments.web.scraping.repository;

import com.ig.comments.web.scraping.model.UserPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {
    List<UserPlan> findByUserId(UUID userId);
}
