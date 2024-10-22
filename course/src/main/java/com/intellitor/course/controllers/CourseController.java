package com.intellitor.course.controllers;

import com.intellitor.common.dtos.CourseDTO;
import com.intellitor.common.utils.Response;
import com.intellitor.course.services.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Response> createCourse(@RequestBody CourseDTO course) {
        return ResponseEntity.ok(courseService.createCourse(course));
    }

    @PutMapping
    public ResponseEntity<Response> updateCourse(@RequestBody CourseDTO course) {
        return ResponseEntity.ok(courseService.updateCourse(course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }
}
