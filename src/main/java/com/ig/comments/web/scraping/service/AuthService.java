package com.ig.comments.web.scraping.service;

import com.ig.comments.web.scraping.dto.AuthResponseDTO;
import com.ig.comments.web.scraping.dto.LoginRequestDTO;
import com.ig.comments.web.scraping.dto.RegisterRequestDTO;
import com.ig.comments.web.scraping.exception.EmailAlreadyExistsException;
import com.ig.comments.web.scraping.exception.InvalidCredentialsException;
import com.ig.comments.web.scraping.exception.ResourceNotFoundException;
import com.ig.comments.web.scraping.model.Permission;
import com.ig.comments.web.scraping.model.RoleCode;
import com.ig.comments.web.scraping.model.User;
import com.ig.comments.web.scraping.model.UserPlan;
import com.ig.comments.web.scraping.repository.PermissionRepository;
import com.ig.comments.web.scraping.repository.PlanRepository;
import com.ig.comments.web.scraping.repository.RoleCodeRepository;
import com.ig.comments.web.scraping.repository.UserPlanRepository;
import com.ig.comments.web.scraping.repository.UserRepository;
import com.ig.comments.web.scraping.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleCodeRepository roleCodeRepository;
    private final PlanRepository planRepository;
    private final UserPlanRepository userPlanRepository;
    private final PermissionRepository permissionRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       RoleCodeRepository roleCodeRepository,
                       PlanRepository planRepository,
                       UserPlanRepository userPlanRepository,
                       PermissionRepository permissionRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleCodeRepository = roleCodeRepository;
        this.planRepository = planRepository;
        this.userPlanRepository = userPlanRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        RoleCode roleCode = roleCodeRepository.findById("PLAN_BEGINNER").orElseThrow(() -> new ResourceNotFoundException("Role code not found"));

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoleCode("PLAN_BEGINNER");
        user = userRepository.save(user);

        var plan = planRepository.findByRole("PLAN_BEGINNER").orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        UserPlan userPlan = new UserPlan();
        userPlan.setUser(user);
        userPlan.setPlan(plan);
        userPlanRepository.save(userPlan);

        Permission permission = new Permission();
        permission.setUser(user);
        permission.setRoleCode(roleCode.getRole());
        permission.setUsageLimit(roleCode.getUsageLimit());
        permission.setRemainingLimit(roleCode.getUsageLimit());
        permission.setCommentsPerQuery(roleCode.getCommentsPerQuery());
        permissionRepository.save(permission);

        String token = jwtService.generateToken(user.getId(), user.getRoleCode());
        return new AuthResponseDTO(token, user.getId());
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId(), user.getRoleCode());
        return new AuthResponseDTO(token, user.getId());
    }
}
