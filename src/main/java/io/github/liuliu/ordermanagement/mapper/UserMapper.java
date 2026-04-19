package io.github.liuliu.ordermanagement.mapper;

import io.github.liuliu.ordermanagement.domain.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface UserMapper {

    Optional<UserEntity> findById(@Param("id") UUID id);

    // only for unittest now
    Optional<UserEntity> insert(UserEntity user);
}
