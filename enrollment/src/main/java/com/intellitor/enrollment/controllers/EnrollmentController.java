package com.intellitor.enrollment.controllers;

import com.intellitor.common.dtos.EnrollmentDTO;
import com.intellitor.common.utils.Response;
import com.intellitor.enrollment.services.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getEnrollment(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Response> createEnrollment(@RequestBody EnrollmentDTO enrollment) {
        return ResponseEntity.ok(enrollmentService.createEnrollment(enrollment));
    }

    @PutMapping
    public ResponseEntity<Response> updateEnrollment(@RequestBody EnrollmentDTO enrollment) {
        return ResponseEntity.ok(enrollmentService.updateEnrollment(enrollment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteEnrollment(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.deleteEnrollment(id));
    }

}
