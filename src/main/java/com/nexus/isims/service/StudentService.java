package com.nexus.isims.service;

import com.nexus.isims.entity.Student;
import com.nexus.isims.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Handles the logic from display.php: Pagination + Search
    public Page<Student> findPaginated(int pageNo, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        if (keyword != null && !keyword.trim().isEmpty()) {
            // If searching, get all matching results and wrap in a Page object
            // (Note: For large DBs, you'd want a true paginated database query, 
            // but this mimics your PHP logic effectively)
            List<Student> searchResults = studentRepository.searchStudents(keyword);

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), searchResults.size());

            if(start > searchResults.size()) {
                return new PageImpl<>(Collections.emptyList(), pageable, searchResults.size());
            }

            List<Student> pageContent = searchResults.subList(start, end);
            return new PageImpl<>(pageContent, pageable, searchResults.size());
        }

        // Default: Get all students with standard pagination
        return studentRepository.findAll(pageable);
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found :: " + id));
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }
}