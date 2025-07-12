package com.playschool.management.service;

import com.playschool.management.entity.Student;
import com.playschool.management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Register a new student in the database
     */
    public Student registerStudent(Student student) {
        // Set default values if not provided
        if (student.getEnrollmentDate() == null) {
            student.setEnrollmentDate(LocalDate.now());
        }
        
        if (student.getIsActive() == null) {
            student.setIsActive(true);
        }
        
        // Set timestamps
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        
        // Save student to database
        return studentRepository.save(student);
    }
    
    /**
     * Get all active students
     */
    public List<Student> getAllActiveStudents() {
        return studentRepository.findByIsActiveTrue();
    }
    
    /**
     * Get student by ID
     */
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    /**
     * Update student information
     */
    public Student updateStudent(Long id, Student updatedStudent) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            
            // Update fields
            student.setFirstName(updatedStudent.getFirstName());
            student.setLastName(updatedStudent.getLastName());
            student.setDateOfBirth(updatedStudent.getDateOfBirth());
            student.setGender(updatedStudent.getGender());
            student.setAddress(updatedStudent.getAddress());
            student.setParentName(updatedStudent.getParentName());
            student.setParentPhone(updatedStudent.getParentPhone());
            student.setParentEmail(updatedStudent.getParentEmail());
            student.setEmergencyContact(updatedStudent.getEmergencyContact());
            student.setEmergencyPhone(updatedStudent.getEmergencyPhone());
            student.setMedicalInfo(updatedStudent.getMedicalInfo());
            student.setAllergies(updatedStudent.getAllergies());
            student.setUpdatedAt(LocalDateTime.now());
            
            return studentRepository.save(student);
        }
        
        throw new RuntimeException("Student not found with id: " + id);
    }
    
    /**
     * Soft delete student (set isActive to false)
     */
    public void deactivateStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        
        if (student.isPresent()) {
            Student studentToDeactivate = student.get();
            studentToDeactivate.setIsActive(false);
            studentToDeactivate.setUpdatedAt(LocalDateTime.now());
            studentRepository.save(studentToDeactivate);
        } else {
            throw new RuntimeException("Student not found with id: " + id);
        }
    }
    
    /**
     * Reactivate student
     */
    public Student reactivateStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        
        if (student.isPresent()) {
            Student studentToReactivate = student.get();
            studentToReactivate.setIsActive(true);
            studentToReactivate.setUpdatedAt(LocalDateTime.now());
            return studentRepository.save(studentToReactivate);
        }
        
        throw new RuntimeException("Student not found with id: " + id);
    }
    
    /**
     * Search students by name
     */
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContaining(name);
    }
    
    /**
     * Get students by parent email
     */
    public List<Student> getStudentsByParentEmail(String parentEmail) {
        return studentRepository.findByParentEmailIgnoreCase(parentEmail);
    }
    
    /**
     * Get total count of active students
     */
    public long getTotalActiveStudents() {
        return studentRepository.findByIsActiveTrue().size();
    }
    
    /**
     * Check if student exists by ID
     */
    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }
    
    /**
     * Get all students (including inactive)
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    /**
     * Validate student data before registration
     */
    public void validateStudentData(Student student) {
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        
        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        
        if (student.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        
        if (student.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
        
        // Calculate age - should be between 1-6 years for playschool
        int age = LocalDate.now().getYear() - student.getDateOfBirth().getYear();
        if (age < 1 || age > 6) {
            throw new IllegalArgumentException("Student age should be between 1-6 years for playschool");
        }
        
        if (student.getParentEmail() != null && !student.getParentEmail().isEmpty()) {
            if (!isValidEmail(student.getParentEmail())) {
                throw new IllegalArgumentException("Invalid parent email format");
            }
        }
    }
    
    /**
     * Helper method to validate email format
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Register student with validation
     */
    public Student registerStudentWithValidation(Student student) {
        validateStudentData(student);
        return registerStudent(student);
    }
}
