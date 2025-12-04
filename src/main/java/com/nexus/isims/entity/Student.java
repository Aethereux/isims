package com.nexus.isims.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", length = 100)
    @NotBlank(message = "Full Name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Full Name must contain only letters and spaces") // From validation.js
    private String fullName;

    @Column(name = "dob")
    @NotNull(message = "Birthday is required")
    private LocalDate dob;

    @Column(name = "gender")
    @NotBlank(message = "Gender is required")
    private String gender; // You can also use an Enum here

    @Column(name = "course", length = 50)
    @NotBlank(message = "Program is required")
    private String course;

    @Column(name = "year_level")
    @NotNull(message = "Year Level is required")
    private Integer yearLevel;

    @Column(name = "contact_number", length = 15)
    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Invalid mobile number") // From validation.js
    private String contactNumber;

    @Column(name = "email", length = 100)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format") // From validation.js
    private String email;

    // Stores the image as binary data (BLOB)
    @Lob
    @Column(name = "student_picture", columnDefinition = "LONGBLOB")
    private byte[] studentPicture;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Student() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public Integer getYearLevel() { return yearLevel; }
    public void setYearLevel(Integer yearLevel) { this.yearLevel = yearLevel; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public byte[] getStudentPicture() { return studentPicture; }
    public void setStudentPicture(byte[] studentPicture) { this.studentPicture = studentPicture; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Helper method to encode image to Base64 for display in Thymeleaf
    public String getStudentPictureBase64() {
        if (studentPicture != null && studentPicture.length > 0) {
            return java.util.Base64.getEncoder().encodeToString(studentPicture);
        }
        return null;
    }
}