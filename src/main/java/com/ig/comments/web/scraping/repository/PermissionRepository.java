package com.ig.comments.web.scraping.repository;

import com.ig.comments.web.scraping.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByUserId(UUID userId);
}
