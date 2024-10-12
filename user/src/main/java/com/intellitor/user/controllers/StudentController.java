package com.intellitor.user.controllers;

import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.utils.Response;
import com.intellitor.user.feign.DaoFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final DaoFeignClient daoFeignClient;

    public StudentController(DaoFeignClient daoFeignClient) {
        this.daoFeignClient = daoFeignClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getStudent(@PathVariable Long id) {
        return daoFeignClient.getStudentById(id);
    }

    @GetMapping("/validate")
    public ResponseEntity<Response> validateStudentByEmailAndPassword(@RequestParam String email, @RequestParam String password) {
        return daoFeignClient.getStudentByEmailAndPassword(email, password);
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerStudent(@RequestBody StudentDTO student) {
        return daoFeignClient.createStudent(student);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateStudent(@RequestBody StudentDTO student) {
        return daoFeignClient.updateStudent(student);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteStudent(@PathVariable Long id) {
        return daoFeignClient.deleteStudentById(id);
    }

}
