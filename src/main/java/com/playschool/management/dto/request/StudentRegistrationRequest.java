package com.playschool.management.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class StudentRegistrationRequest {
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @Size(max = 10, message = "Gender must not exceed 10 characters")
    private String gender;
    
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;
    
    @Size(max = 100, message = "Parent name must not exceed 100 characters")
    private String parentName;
    
    @Size(max = 15, message = "Parent phone must not exceed 15 characters")
    @Pattern(regexp = "^[+]?[0-9\\-\\s()]*$", message = "Invalid phone number format")
    private String parentPhone;
    
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Parent email must not exceed 100 characters")
    private String parentEmail;
    
    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    private String emergencyContact;
    
    @Size(max = 15, message = "Emergency phone must not exceed 15 characters")
    @Pattern(regexp = "^[+]?[0-9\\-\\s()]*$", message = "Invalid phone number format")
    private String emergencyPhone;
    
    @Size(max = 500, message = "Medical info must not exceed 500 characters")
    private String medicalInfo;
    
    @Size(max = 500, message = "Allergies info must not exceed 500 characters")
    private String allergies;
    
    private LocalDate enrollmentDate;
    
    // Constructors
    public StudentRegistrationRequest() {}
    
    public StudentRegistrationRequest(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }
    
    // Getters and Setters
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
}
