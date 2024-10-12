package com.intellitor.user.feign;

import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "dao")
public interface DaoFeignClient {

    /* ------------------------------------------------------------------------------
     ** Teacher APIs
     * ------------------------------------------------------------------------------ */

    @GetMapping("/teacher/{id}")
    ResponseEntity<Response> getTeacherById(@PathVariable Long id);

    @GetMapping("/teacher")
    ResponseEntity<Response> getTeacherByEmailAndPassword(@RequestParam String email, @RequestParam String password);

    @PostMapping("/teacher")
    ResponseEntity<Response> createTeacher(@RequestBody TeacherDTO teacher);

    @PutMapping("/teacher")
    ResponseEntity<Response> updateTeacher(@RequestBody TeacherDTO teacher);

    @DeleteMapping("/teacher/{id}")
    ResponseEntity<Response> deleteTeacherById(@PathVariable Long id);

    /* ------------------------------------------------------------------------------
     ** Student APIs
     * ------------------------------------------------------------------------------ */

    @GetMapping("/student/{id}")
    ResponseEntity<Response> getStudentById(@PathVariable Long id);

    @GetMapping("/student")
    ResponseEntity<Response> getStudentByEmailAndPassword(@RequestParam String email, @RequestParam String password);

    @PostMapping("/student")
    ResponseEntity<Response> createStudent(@RequestBody StudentDTO student);

    @PutMapping("/student")
    ResponseEntity<Response> updateStudent(@RequestBody StudentDTO student);

    @DeleteMapping("/student/{id}")
    ResponseEntity<Response> deleteStudentById(@PathVariable Long id);
}
