package com.playschool.management.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playschool.management.dto.response.MessageResponse;
import com.playschool.management.entity.Role;
import com.playschool.management.entity.RoleName;
import com.playschool.management.entity.User;
import com.playschool.management.service.UserRoleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {
    
    @Autowired
    private UserRoleService userRoleService;
    
    /**
     * Add role to user
     */
    @PostMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long userId, 
                                          @PathVariable String roleName) {
        try {
            RoleName role = RoleName.valueOf("ROLE_" + roleName.toUpperCase());
            User updatedUser = userRoleService.addRoleToUser(userId, role);
            return ResponseEntity.ok(new MessageResponse("Role " + roleName + " added to user successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    /**
     * Remove role from user
     */
    @DeleteMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, 
                                               @PathVariable String roleName) {
        try {
            RoleName role = RoleName.valueOf("ROLE_" + roleName.toUpperCase());
            User updatedUser = userRoleService.removeRoleFromUser(userId, role);
            return ResponseEntity.ok(new MessageResponse("Role " + roleName + " removed from user successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    /**
     * Get all roles for a user
     */
    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Set<Role>> getUserRoles(@PathVariable Long userId) {
        Set<Role> roles = userRoleService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }
    
    /**
     * Get all users with a specific role
     */
    @GetMapping("/roles/{roleName}/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String roleName) {
        try {
            RoleName role = RoleName.valueOf("ROLE_" + roleName.toUpperCase());
            List<User> users = userRoleService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Check if user has a specific role
     */
    @GetMapping("/{userId}/has-role/{roleName}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Boolean> userHasRole(@PathVariable Long userId, 
                                              @PathVariable String roleName) {
        try {
            RoleName role = RoleName.valueOf("ROLE_" + roleName.toUpperCase());
            boolean hasRole = userRoleService.userHasRole(userId, role);
            return ResponseEntity.ok(hasRole);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
