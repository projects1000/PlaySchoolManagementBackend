package com.playschool.management.service;

import com.playschool.management.entity.Role;
import com.playschool.management.entity.RoleName;
import com.playschool.management.entity.User;
import com.playschool.management.repository.RoleRepository;
import com.playschool.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserRoleService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    /**
     * Add a role to a user
     */
    public User addRoleToUser(Long userId, RoleName roleName) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        
        if (userOpt.isPresent() && roleOpt.isPresent()) {
            User user = userOpt.get();
            Role role = roleOpt.get();
            
            user.getRoles().add(role);
            return userRepository.save(user);
        }
        
        throw new RuntimeException("User or Role not found");
    }
    
    /**
     * Remove a role from a user
     */
    public User removeRoleFromUser(Long userId, RoleName roleName) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        
        if (userOpt.isPresent() && roleOpt.isPresent()) {
            User user = userOpt.get();
            Role role = roleOpt.get();
            
            user.getRoles().remove(role);
            return userRepository.save(user);
        }
        
        throw new RuntimeException("User or Role not found");
    }
    
    /**
     * Set multiple roles for a user (replaces existing roles)
     */
    public User setUserRoles(Long userId, Set<RoleName> roleNames) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Set<Role> roles = new HashSet<>();
            
            for (RoleName roleName : roleNames) {
                Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
            
            user.setRoles(roles);
            return userRepository.save(user);
        }
        
        throw new RuntimeException("User not found");
    }
    
    /**
     * Get all users with a specific role
     */
    public List<User> getUsersByRole(RoleName roleName) {
        return userRepository.findAll().stream()
            .filter(user -> user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName)))
            .toList();
    }
    
    /**
     * Check if user has a specific role
     */
    public boolean userHasRole(Long userId, RoleName roleName) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
        }
        
        return false;
    }
    
    /**
     * Get all roles for a user
     */
    public Set<Role> getUserRoles(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            return userOpt.get().getRoles();
        }
        
        return new HashSet<>();
    }
}
