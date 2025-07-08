package com.playschool.management.repository;

import com.playschool.management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    List<Student> findByIsActiveTrue();
    
    @Query("SELECT s FROM Student s WHERE s.firstName LIKE %:name% OR s.lastName LIKE %:name%")
    List<Student> findByNameContaining(@Param("name") String name);
    
    List<Student> findByParentEmailIgnoreCase(String parentEmail);
    
    @Query("SELECT s FROM Student s WHERE s.isActive = true ORDER BY s.firstName, s.lastName")
    List<Student> findAllActiveStudentsOrderedByName();
}
