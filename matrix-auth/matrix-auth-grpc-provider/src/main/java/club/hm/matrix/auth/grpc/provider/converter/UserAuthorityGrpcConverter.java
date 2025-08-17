package club.hm.matrix.auth.grpc.provider.converter;

import club.hm.matrix.auth.data.dto.UserDTO;
import club.hm.matrix.auth.grpc.*;
import org.springframework.stereotype.Component;

import static club.hm.matrix.shared.grpc.base.utils.TimeStamp.fromTimestamp;
import static club.hm.matrix.shared.grpc.base.utils.TimeStamp.toTimestamp;

@Component
public class UserAuthorityGrpcConverter {


    /**
     * UserDTO -> gRPC User
     */
    public UserResponse toGrpcUser(UserDTO userDTO) {
        if (userDTO == null) return UserResponse.getDefaultInstance();

        var builder = User.newBuilder()
                .setId(userDTO.getId() != null ? userDTO.getId() : 0)
                .setUsername(userDTO.getUsername() != null ? userDTO.getUsername() : "")
                .setPassword(userDTO.getPassword() != null ? userDTO.getPassword() : "")
                .setEnabled(userDTO.getEnabled() != null ? userDTO.getEnabled() : false);

        if (userDTO.getEmail() != null) {
            builder.setEmail(userDTO.getEmail());
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
    public UserDTO fromGrpcUser(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .createdAt(fromTimestamp(user.getCreatedAt()))
                .updatedAt(fromTimestamp(user.getUpdatedAt()))
                .build();
    }
}