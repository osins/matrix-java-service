package osins.matrix.auth.grpc.provider.converter;

import osins.matrix.auth.data.dto.UserDTO;
import osins.matrix.auth.grpc.*;
import org.springframework.stereotype.Component;

import static osins.matrix.shared.grpc.base.utils.TimeStamp.toTimestamp;

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
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }

    public User toUser(UserDTO userDTO) {
        if (userDTO == null) return null;
        return User.newBuilder()
                .setId(userDTO.getId())
                .setUsername(userDTO.getUsername())
                .setPassword(userDTO.getPassword())
                .setEnabled(userDTO.getEnabled())
                .setCreatedAt(toTimestamp(userDTO.getCreatedAt()))
                .setUpdatedAt(toTimestamp(userDTO.getUpdatedAt()))
                .build();
    }
}