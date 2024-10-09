package com.intellitor.user.controllers;

import com.intellitor.common.dtos.UserDTO;
import com.intellitor.common.utils.Response;
import com.intellitor.user.feign.DaoFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final DaoFeignClient daoFeignClient;

    public UserController(DaoFeignClient daoFeignClient) {
        this.daoFeignClient = daoFeignClient;
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserDTO user) {
        return daoFeignClient.createUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUser(@PathVariable Long id) {
        return daoFeignClient.getUserById(id);
    }
}
