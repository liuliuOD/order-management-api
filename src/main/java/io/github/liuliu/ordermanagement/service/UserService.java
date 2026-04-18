package io.github.liuliu.ordermanagement.service;

import io.github.liuliu.ordermanagement.exception.ResourceNotFoundException;
import io.github.liuliu.ordermanagement.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Storage storage;

    @Transactional
    public void deleteUser(UUID userId) {
        storage.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        storage.deleteUserCompletely(userId);
    }
}
