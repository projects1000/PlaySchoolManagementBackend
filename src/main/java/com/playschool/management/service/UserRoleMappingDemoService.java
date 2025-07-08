package com.playschool.management.service;

import com.playschool.management.entity.Role;
import com.playschool.management.entity.RoleName;
import com.playschool.management.entity.User;
import com.playschool.management.repository.RoleRepository;
import com.playschool.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleMappingDemoService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    /**
     * This method demonstrates exactly how user.id syncs with role.id
     * in the user_roles junction table
     */
    public void demonstrateUserRoleSync() {
        // 1. Get user by ID - this user.id will be used as user_id in junction table
        User user = userRepository.findById(1L).orElse(null);
        if (user != null) {
            System.out.println("User ID: " + user.getId() + " (this becomes user_id in user_roles table)");
            System.out.println("Username: " + user.getUsername());
        }
        
        // 2. Get role by name - this role.id will be used as role_id in junction table  
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(null);
        if (adminRole != null) {
            System.out.println("Role ID: " + adminRole.getId() + " (this becomes role_id in user_roles table)");
            System.out.println("Role Name: " + adminRole.getName());
        }
        
        // 3. When we add role to user, JPA creates the mapping
        if (user != null && adminRole != null) {
            user.getRoles().add(adminRole);
            userRepository.save(user);
            
            System.out.println("JPA will execute: INSERT INTO user_roles (user_id, role_id) VALUES (" 
                             + user.getId() + ", " + adminRole.getId() + ")");
        }
        
        // 4. Show the actual roles for verification
        if (user != null) {
            User savedUser = userRepository.findById(user.getId()).get();
            System.out.println("User " + savedUser.getUsername() + " now has roles:");
            savedUser.getRoles().forEach(role -> 
                System.out.println("- Role ID: " + role.getId() + ", Name: " + role.getName())
            );
        }
    }
    
    /**
     * This method shows the database records that would be created
     */
    public void showJunctionTableMapping(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            System.out.println("\nJunction table user_roles records for user " + user.getUsername() + ":");
            System.out.println("user_id | role_id | (meaning)");
            System.out.println("--------|---------|----------");
            
            user.getRoles().forEach(role -> {
                System.out.println(String.format("%7d | %7d | User '%s' has role '%s'", 
                    user.getId(), 
                    role.getId(), 
                    user.getUsername(), 
                    role.getName().toString().replace("ROLE_", "")
                ));
            });
        }
    }
}
