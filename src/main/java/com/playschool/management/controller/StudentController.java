package com.playschool.management.controller;

import com.playschool.management.entity.Student;
import com.playschool.management.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STAFF')")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAllActiveStudentsOrderedByName();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STAFF')")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.ok(savedStudent);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            
            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            student.setDateOfBirth(studentDetails.getDateOfBirth());
            student.setGender(studentDetails.getGender());
            student.setAddress(studentDetails.getAddress());
            student.setParentName(studentDetails.getParentName());
            student.setParentPhone(studentDetails.getParentPhone());
            student.setParentEmail(studentDetails.getParentEmail());
            student.setEmergencyContact(studentDetails.getEmergencyContact());
            student.setEmergencyPhone(studentDetails.getEmergencyPhone());
            student.setMedicalInfo(studentDetails.getMedicalInfo());
            student.setAllergies(studentDetails.getAllergies());
            student.setIsActive(studentDetails.getIsActive());
            
            Student updatedStudent = studentRepository.save(student);
            return ResponseEntity.ok(updatedStudent);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            // Soft delete - set isActive to false instead of deleting
            Student studentToUpdate = student.get();
            studentToUpdate.setIsActive(false);
            studentRepository.save(studentToUpdate);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STAFF')")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String name) {
        List<Student> students = studentRepository.findByNameContaining(name);
        return ResponseEntity.ok(students);
    }
}
