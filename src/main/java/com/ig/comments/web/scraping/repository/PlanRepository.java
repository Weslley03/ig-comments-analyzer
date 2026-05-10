package com.ig.comments.web.scraping.repository;

import com.ig.comments.web.scraping.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByRole(String role);
}
