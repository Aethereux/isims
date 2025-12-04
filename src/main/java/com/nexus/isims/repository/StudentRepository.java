package com.nexus.isims.repository;

import com.nexus.isims.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Custom query to search by name, course, or year level
    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.course) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(s.yearLevel AS string) LIKE :keyword")
    List<Student> searchStudents(@Param("keyword") String keyword);
}