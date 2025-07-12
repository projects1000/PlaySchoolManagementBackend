package com.playschool.management.dto.response;

import com.playschool.management.entity.Student;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudentResponse {
    
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String parentName;
    private String parentPhone;
    private String parentEmail;
    private String emergencyContact;
    private String emergencyPhone;
    private String medicalInfo;
    private String allergies;
    private LocalDate enrollmentDate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public StudentResponse() {}
    
    public StudentResponse(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.dateOfBirth = student.getDateOfBirth();
        this.gender = student.getGender();
        this.address = student.getAddress();
        this.parentName = student.getParentName();
        this.parentPhone = student.getParentPhone();
        this.parentEmail = student.getParentEmail();
        this.emergencyContact = student.getEmergencyContact();
        this.emergencyPhone = student.getEmergencyPhone();
        this.medicalInfo = student.getMedicalInfo();
        this.allergies = student.getAllergies();
        this.enrollmentDate = student.getEnrollmentDate();
        this.isActive = student.getIsActive();
        this.createdAt = student.getCreatedAt();
        this.updatedAt = student.getUpdatedAt();
    }
    
    // Helper method to convert Student entity to StudentResponse
    public static StudentResponse fromStudent(Student student) {
        return new StudentResponse(student);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    
    public String getParentPhone() { return parentPhone; }
    public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }
    
    public String getParentEmail() { return parentEmail; }
    public void setParentEmail(String parentEmail) { this.parentEmail = parentEmail; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
    
    public String getMedicalInfo() { return medicalInfo; }
    public void setMedicalInfo(String medicalInfo) { this.medicalInfo = medicalInfo; }
    
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    // Helper method to calculate age
    public int getAge() {
        if (dateOfBirth != null) {
            return LocalDate.now().getYear() - dateOfBirth.getYear();
        }
        return 0;
    }
}
