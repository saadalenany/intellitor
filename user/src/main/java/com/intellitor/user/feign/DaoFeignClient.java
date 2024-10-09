package com.intellitor.user.feign;

import com.intellitor.common.dtos.UserDTO;
import com.intellitor.common.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dao-service")
public interface DaoFeignClient {

    @GetMapping("/teacher/{id}")
    ResponseEntity<Response> getUserById(@PathVariable Long id);

    @PostMapping("/teacher")
    ResponseEntity<Response> createUser(@RequestBody UserDTO user);
}
