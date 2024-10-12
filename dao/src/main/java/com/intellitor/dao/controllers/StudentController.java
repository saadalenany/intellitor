package com.intellitor.dao.controllers;

import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Response> getStudentByEmailAndPassword(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(studentService.findByEmailAndPassword(email, password));
    }

    @PostMapping
    public ResponseEntity<Response> createStudent(@RequestBody StudentDTO student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @PutMapping
    public ResponseEntity<Response> updateStudent(@RequestBody StudentDTO student) {
        return ResponseEntity.ok(studentService.updateStudent(student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }

}
