package com.nexus.isims.controller;

import com.nexus.isims.entity.Student;
import com.nexus.isims.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Replaces display.php
    @GetMapping("/")
    public String viewHomePage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String search,
            Model model) {

        int pageSize = 5; // Matches $recordsPerPage = 5 in display.php
        Page<Student> studentPage = studentService.findPaginated(page, pageSize, search);

        model.addAttribute("listStudents", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("search", search);

        return "display"; // Maps to resources/templates/display.html
    }

    // Replaces form.php (Show Form)
    @GetMapping("/form")
    public String showNewStudentForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "form"; // Maps to resources/templates/form.html
    }

    // Replaces insert.php and update_student.php (Process Data)
    @PostMapping("/save")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        // Server-side validation check
        if (bindingResult.hasErrors()) {
            return "form"; // Return to form if there are errors
        }

        try {
            // Handle Image Upload if a new file is provided
            if (!file.isEmpty()) {
                student.setStudentPicture(file.getBytes());
            } else {
                // If updating and no new image, keep the old one (logic needed inside service or check hidden field)
                if (student.getId() != null) {
                    Student existing = studentService.getStudentById(student.getId());
                    if (existing.getStudentPicture() != null) {
                        student.setStudentPicture(existing.getStudentPicture());
                    }
                }
            }

            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("message", "Student record saved successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Error uploading image");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/"; // Redirect to display page
    }

    // Replaces update_student.php (Show Update Form)
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable(value = "id") Long id, Model model) {
        try {
            Student student = studentService.getStudentById(id);
            model.addAttribute("student", student);
            return "form"; // Reuses the same form template
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    // Replaces delete_student.php
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes) {
        this.studentService.deleteStudentById(id);
        redirectAttributes.addFlashAttribute("message", "Student record deleted successfully.");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/";
    }

    // Replaces display_image.php
    @GetMapping("/image/{id}")
    public void showStudentImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        Student student = studentService.getStudentById(id);
        if (student.getStudentPicture() != null) {
            response.setContentType("image/jpeg");
            response.getOutputStream().write(student.getStudentPicture());
            response.getOutputStream().close();
        }
    }
}