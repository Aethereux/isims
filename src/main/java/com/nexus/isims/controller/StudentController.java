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

    // Inside StudentController.java
    @GetMapping("/landingPage")
    public String showLandingPage() {
        // Spring will look for src/main/resources/templates/landingPage.html
        return "landingPage";
    }

    @GetMapping("/")
    public String viewHomePage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String search,
            Model model) {

        int pageSize = 5;
        Page<Student> studentPage = studentService.findPaginated(page, pageSize, search);

        model.addAttribute("listStudents", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("search", search);

        return "display";
    }

    @GetMapping("/form")
    public String showNewStudentForm(Model model) {
        if (!model.containsAttribute("student")) {
            model.addAttribute("student", new Student());
        }
        return "form";
    }

    @PostMapping("/save")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "form";
        }

        try {
            if (!file.isEmpty()) {
                student.setStudentPicture(file.getBytes());
            } else if (student.getId() != null) {
                Student existing = studentService.getStudentById(student.getId());
                if (existing.getStudentPicture() != null) {
                    student.setStudentPicture(existing.getStudentPicture());
                }
            }

            boolean isNew = (student.getId() == null);
            studentService.saveStudent(student);

            if (isNew) {
                // If creating, stay on form (like PHP insert.php)
                redirectAttributes.addFlashAttribute("message", "Student record added successfully!");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/form";
            } else {
                // If updating, go to list (like PHP update_student.php)
                redirectAttributes.addFlashAttribute("message", "Student record updated successfully!");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/";
            }

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Error uploading image");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/form";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable(value = "id") Long id, Model model) {
        try {
            Student student = studentService.getStudentById(id);
            model.addAttribute("student", student);
            return "form";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes) {
        this.studentService.deleteStudentById(id);
        redirectAttributes.addFlashAttribute("message", "Student record deleted successfully.");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/";
    }

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