package com.playschool.management.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.playschool.management.dto.request.LoginRequest;
import com.playschool.management.dto.request.SignupRequest;
import com.playschool.management.dto.response.JwtResponse;
import com.playschool.management.dto.response.MessageResponse;
import com.playschool.management.entity.Role;
import com.playschool.management.entity.RoleName;
import com.playschool.management.entity.User;
import com.playschool.management.repository.RoleRepository;
import com.playschool.management.repository.UserRepository;
import com.playschool.management.security.jwt.JwtUtils;
import com.playschool.management.security.services.UserPrincipal;

import jakarta.validation.Valid;

@CrossOrigin(
    origins = {"https://playschool-a2z.netlify.app", "http://localhost:3000", "http://localhost:4200"}, 
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowedHeaders = {"*"},
    allowCredentials = "true",
    maxAge = 3600
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtUtils jwtUtils;
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                roles));
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                           signUpRequest.getEmail(),
                           encoder.encode(signUpRequest.getPassword()),
                           signUpRequest.getFirstName(),
                           signUpRequest.getLastName());
        
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleName.ROLE_PARENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "teacher" -> {
                        Role teacherRole = roleRepository.findByName(RoleName.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(teacherRole);
                    }
                    case "staff" -> {
                        Role staffRole = roleRepository.findByName(RoleName.ROLE_STAFF)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(staffRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(RoleName.ROLE_PARENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        
        user.setRoles(roles);
        userRepository.save(user);
        
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    
    @PostMapping("/test-signin")
    public ResponseEntity<?> testAuthEndpoint() {
        return ResponseEntity.ok(new MessageResponse("Auth endpoint is working! Backend authentication is ready."));
    }
    
    // Quick admin creation for testing (REMOVE IN PRODUCTION!)
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdminUser() {
        try {
            // Check if admin already exists
            if (userRepository.existsByUsername("admin")) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Admin user already exists!"));
            }
            
            // Create admin user
            User adminUser = new User("admin", 
                                    "admin@playschool.com", 
                                    encoder.encode("admin123"), 
                                    "Admin", 
                                    "User");
            
            adminUser.setPhoneNumber("1234567890");
            
            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Admin role not found."));
            roles.add(adminRole);
            
            adminUser.setRoles(roles);
            userRepository.save(adminUser);
            
            return ResponseEntity.ok(new MessageResponse("Admin user created successfully! Username: admin, Password: admin123"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error creating admin: " + e.getMessage()));
        }
    }
    
    // Health check endpoint for Render deployment
    @PostMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(new MessageResponse("Backend is healthy and running!"));
    }
    
    // Test CORS endpoint
    @PostMapping("/test-cors")
    public ResponseEntity<?> testCors() {
        return ResponseEntity.ok(new MessageResponse("CORS is working correctly!"));
    }
    
    // Explicit OPTIONS handling for CORS preflight requests
    @RequestMapping(value = "/signin", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleSigninOptions() {
        return ResponseEntity.ok().build();
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleSignupOptions() {
        return ResponseEntity.ok().build();
    }
    
    // Authentication status check endpoint - use this instead of signup for page load
    @GetMapping("/status")
    public ResponseEntity<?> checkAuthStatus() {
        return ResponseEntity.ok(new MessageResponse("Authentication service is available"));
    }
    
    // Get current user info (requires authentication)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return ResponseEntity.ok(new JwtResponse(null, // Don't send token back
                    userPrincipal.getId(),
                    userPrincipal.getUsername(),
                    userPrincipal.getEmail(),
                    userPrincipal.getFirstName(),
                    userPrincipal.getLastName(),
                    userPrincipal.getAuthorities().stream()
                            .map(authority -> authority.getAuthority())
                            .collect(Collectors.toList())));
        }
        return ResponseEntity.status(401).body(new MessageResponse("Not authenticated"));
    }
}
