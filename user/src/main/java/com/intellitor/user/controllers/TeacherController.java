package com.intellitor.user.controllers;

import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.utils.Response;
import com.intellitor.user.feign.DaoFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final DaoFeignClient daoFeignClient;

    public TeacherController(DaoFeignClient daoFeignClient) {
        this.daoFeignClient = daoFeignClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTeacher(@PathVariable Long id) {
        return daoFeignClient.getTeacherById(id);
    }

    @GetMapping("/validate")
    public ResponseEntity<Response> validateTeacherByEmailAndPassword(@RequestParam String email, @RequestParam String password) {
        return daoFeignClient.getTeacherByEmailAndPassword(email, password);
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerTeacher(@RequestBody TeacherDTO teacher) {
        return daoFeignClient.createTeacher(teacher);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateTeacher(@RequestBody TeacherDTO teacher) {
        return daoFeignClient.updateTeacher(teacher);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteTeacher(@PathVariable Long id) {
        return daoFeignClient.deleteTeacherById(id);
    }

}
