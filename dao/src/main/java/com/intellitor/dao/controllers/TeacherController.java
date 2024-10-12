package com.intellitor.dao.controllers;

import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.services.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Response> getTeacherByEmailAndPassword(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(teacherService.findByEmailAndPassword(email, password));
    }

    @PostMapping
    public ResponseEntity<Response> createTeacher(@RequestBody TeacherDTO teacher) {
        return ResponseEntity.ok(teacherService.createTeacher(teacher));
    }

    @PutMapping
    public ResponseEntity<Response> updateTeacher(@RequestBody TeacherDTO teacher) {
        return ResponseEntity.ok(teacherService.updateTeacher(teacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.deleteTeacher(id));
    }

}
