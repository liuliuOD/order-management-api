package io.github.liuliu.ordermanagement.controller;

import io.github.liuliu.api.UserApi;
import io.github.liuliu.ordermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<Void> deleteUserById(UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
