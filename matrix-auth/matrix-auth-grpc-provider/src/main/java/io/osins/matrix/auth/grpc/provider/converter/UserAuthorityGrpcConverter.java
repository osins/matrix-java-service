package io.osins.matrix.auth.grpc.provider.converter;

import io.osins.matrix.auth.data.dto.UserDTO;
import io.osins.matrix.auth.grpc.*;
import io.osins.matrix.auth.grpc.CreateUserRequest;
import io.osins.matrix.auth.grpc.User;
import io.osins.matrix.auth.grpc.UserResponse;
import org.springframework.stereotype.Component;

import static io.osins.matrix.shared.grpc.base.utils.TimeStamp.toTimestamp;

@Component
public class UserAuthorityGrpcConverter {

    /**
     * UserDTO -> gRPC User
     */
    public UserResponse toGrpcUser(UserDTO userDTO) {
        if (userDTO == null) return UserResponse.getDefaultInstance();

        var builder = User.newBuilder()
                .setId(userDTO.getId() != null ? userDTO.getId() : 0)
                .setUserId(userDTO.getUserId() != null ? userDTO.getUserId() : "")
                .setUsername(userDTO.getUsername() != null ? userDTO.getUsername() : "")
                .setPassword(userDTO.getPassword() != null ? userDTO.getPassword() : "")
                .setEnabled(userDTO.getEnabled() != null ? userDTO.getEnabled() : false);

        // 设置新增字段
        if (userDTO.getUserId() != null) {
            builder.setUserId(userDTO.getUserId());
        }
        if (userDTO.getEmail() != null) {
            builder.setEmail(userDTO.getEmail());
        }
        if (userDTO.getDisplayName() != null) {
            builder.setDisplayName(userDTO.getDisplayName());
        }
        if (userDTO.getAvatarUrl() != null) {
            builder.setAvatarUrl(userDTO.getAvatarUrl());
        }
        if (userDTO.getIsActive() != null) {
            builder.setIsActive(userDTO.getIsActive());
        }

        if (userDTO.getCreatedAt() != null) {
            builder.setCreatedAt(toTimestamp(userDTO.getCreatedAt()));
        }

        if (userDTO.getUpdatedAt() != null) {
            builder.setUpdatedAt(toTimestamp(userDTO.getUpdatedAt()));
        }

        return UserResponse.newBuilder().setUser(builder.build()).build();
    }

    /**
     * gRPC User -> UserDTO
     */
    public UserDTO fromGrpcUser(CreateUserRequest request) {
        if (request == null) return null;

        return UserDTO.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }

    public User toUser(UserDTO userDTO) {
        if (userDTO == null) return null;
        var builder = User.newBuilder()
                .setId(userDTO.getId())
                .setUsername(userDTO.getUsername())
                .setPassword(userDTO.getPassword())
                .setEnabled(userDTO.getEnabled())
                .setCreatedAt(toTimestamp(userDTO.getCreatedAt()))
                .setUpdatedAt(toTimestamp(userDTO.getUpdatedAt()));

        // 添加新增字段
        if (userDTO.getUserId() != null) {
            builder.setUserId(userDTO.getUserId());
        }
        if (userDTO.getEmail() != null) {
            builder.setEmail(userDTO.getEmail());
        }
        if (userDTO.getDisplayName() != null) {
            builder.setDisplayName(userDTO.getDisplayName());
        }
        if (userDTO.getAvatarUrl() != null) {
            builder.setAvatarUrl(userDTO.getAvatarUrl());
        }
        if (userDTO.getIsActive() != null) {
            builder.setIsActive(userDTO.getIsActive());
        }

        return builder.build();
    }
}