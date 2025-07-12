package com.playschool.management.controller;

import com.playschool.management.dto.request.StudentRegistrationRequest;
import com.playschool.management.dto.response.MessageResponse;
import com.playschool.management.dto.response.StudentResponse;
import com.playschool.management.entity.Student;
import com.playschool.management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STAFF')")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<Student> students = studentService.getAllActiveStudents();
        List<StudentResponse> studentResponses = students.stream()
                .map(StudentResponse::fromStudent)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentResponses);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STAFF')")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(StudentResponse.fromStudent(student.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        try {
            // Convert DTO to Entity
            Student student = new Student();
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setDateOfBirth(request.getDateOfBirth());
            student.setGender(request.getGender());
            student.setAddress(request.getAddress());
            student.setParentName(request.getParentName());
            student.setParentPhone(request.getParentPhone());
            student.setParentEmail(request.getParentEmail());
            student.setEmergencyContact(request.getEmergencyContact());
            student.setEmergencyPhone(request.getEmergencyPhone());
            student.setMedicalInfo(request.getMedicalInfo());
            student.setAllergies(request.getAllergies());
            student.setEnrollmentDate(request.getEnrollmentDate());
            
            // Register student with validation
            Student registeredStudent = studentService.registerStudentWithValidation(student);
            
            return ResponseEntity.ok(StudentResponse.fromStudent(registeredStudent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Validation Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Registration Error: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRegistrationRequest request) {
        try {
            // Convert DTO to Entity
            Student studentDetails = new Student();
            studentDetails.setFirstName(request.getFirstName());
            studentDetails.setLastName(request.getLastName());
            studentDetails.setDateOfBirth(request.getDateOfBirth());
            studentDetails.setGender(request.getGender());
            studentDetails.setAddress(request.getAddress());
            studentDetails.setParentName(request.getParentName());
            studentDetails.setParentPhone(request.getParentPhone());
            studentDetails.setParentEmail(request.getParentEmail());
            studentDetails.setEmergencyContact(request.getEmergencyContact());
            studentDetails.setEmergencyPhone(request.getEmergencyPhone());
            studentDetails.setMedicalInfo(request.getMedicalInfo());
            studentDetails.setAllergies(request.getAllergies());
            
            Student updatedStudent = studentService.updateStudent(id, studentDetails);
            return ResponseEntity.ok(StudentResponse.fromStudent(updatedStudent));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Update Error: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deactivateStudent(id);
            return ResponseEntity.ok(new MessageResponse("Student deactivated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Delete Error: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reactivateStudent(@PathVariable Long id) {
        try {
            Student reactivatedStudent = studentService.reactivateStudent(id);
            return ResponseEntity.ok(StudentResponse.fromStudent(reactivatedStudent));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Reactivation Error: " + e.getMessage()));
        }
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STAFF')")
    public ResponseEntity<List<StudentResponse>> searchStudents(@RequestParam String name) {
        List<Student> students = studentService.searchStudentsByName(name);
        List<StudentResponse> studentResponses = students.stream()
                .map(StudentResponse::fromStudent)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentResponses);
    }
    
    @GetMapping("/parent/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STAFF') or hasRole('PARENT')")
    public ResponseEntity<List<StudentResponse>> getStudentsByParentEmail(@PathVariable String email) {
        List<Student> students = studentService.getStudentsByParentEmail(email);
        List<StudentResponse> studentResponses = students.stream()
                .map(StudentResponse::fromStudent)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentResponses);
    }
    
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STAFF')")
    public ResponseEntity<Long> getTotalActiveStudents() {
        long count = studentService.getTotalActiveStudents();
        return ResponseEntity.ok(count);
    }
    
    // Public endpoint for testing (no authentication required)
    @GetMapping("/public/count")
    public ResponseEntity<Long> getPublicStudentCount() {
        long count = studentService.getTotalActiveStudents();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/public/test")
    public ResponseEntity<String> testStudentApi() {
        return ResponseEntity.ok("Student API is working! Total students: " + studentService.getTotalActiveStudents());
    }
}
