package io.github.liuliu.ordermanagement.converter;

import io.github.liuliu.ordermanagement.domain.dto.DeleteUserCommandDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserConverter {

    public DeleteUserCommandDto toDeleteUserCommandDto(UUID userId) {
        return DeleteUserCommandDto.builder()
                .userId(userId)
                .build();
    }

    public ResponseEntity<Void> toDeleteUserResponse() {
        return ResponseEntity.noContent().build();
    }
}
