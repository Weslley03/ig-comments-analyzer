package com.ig.comments.web.scraping.service;

import com.ig.comments.web.scraping.dto.AssignPlanRequestDTO;
import com.ig.comments.web.scraping.dto.UserPlanResponseDTO;
import com.ig.comments.web.scraping.exception.PlanNotFoundException;
import com.ig.comments.web.scraping.model.Permission;
import com.ig.comments.web.scraping.model.RoleCode;
import com.ig.comments.web.scraping.model.UserPlan;
import com.ig.comments.web.scraping.repository.PermissionRepository;
import com.ig.comments.web.scraping.repository.PlanRepository;
import com.ig.comments.web.scraping.repository.RoleCodeRepository;
import com.ig.comments.web.scraping.repository.UserPlanRepository;
import com.ig.comments.web.scraping.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserPlanService {

    private final UserPlanRepository userPlanRepository;
    private final PlanRepository planRepository;
    private final PermissionRepository permissionRepository;
    private final RoleCodeRepository roleCodeRepository;
    private final UserRepository userRepository;

    public UserPlanService(UserPlanRepository userPlanRepository,
                           PlanRepository planRepository,
                           PermissionRepository permissionRepository,
                           RoleCodeRepository roleCodeRepository,
                           UserRepository userRepository) {
        this.userPlanRepository = userPlanRepository;
        this.planRepository = planRepository;
        this.permissionRepository = permissionRepository;
        this.roleCodeRepository = roleCodeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserPlanResponseDTO assign(UUID userId, AssignPlanRequestDTO dto) {
        var plan = planRepository.findById(dto.planId()).orElseThrow(() -> new PlanNotFoundException("Plan not found"));

        var userRef = userRepository.getReferenceById(userId);

        UserPlan userPlan = new UserPlan();
        userPlan.setUser(userRef);
        userPlan.setPlan(plan);
        userPlan = userPlanRepository.save(userPlan);

        RoleCode roleCode = roleCodeRepository.findById(plan.getRole()).orElseThrow(() -> new PlanNotFoundException("Role code not found for plan"));

        Permission permission = permissionRepository.findByUserId(userId).orElse(new Permission());
        permission.setUser(userRef);
        permission.setRoleCode(plan.getRole());
        permission.setUsageLimit(roleCode.getUsageLimit());
        permission.setRemainingLimit(roleCode.getUsageLimit());
        permission.setCommentsPerQuery(roleCode.getCommentsPerQuery());
        permissionRepository.save(permission);

        return new UserPlanResponseDTO(
                userPlan.getId(),
                userId,
                plan.getId(),
                plan.getDescription(),
                plan.getRole(),
                userPlan.getCreatedAt()
        );
    }

    public List<UserPlanResponseDTO> listByUser(UUID userId) {
        return userPlanRepository.findByUserId(userId).stream()
                .map(up -> new UserPlanResponseDTO(
                        up.getId(),
                        up.getUser().getId(),
                        up.getPlan().getId(),
                        up.getPlan().getDescription(),
                        up.getPlan().getRole(),
                        up.getCreatedAt()
                ))
                .toList();
    }
}
