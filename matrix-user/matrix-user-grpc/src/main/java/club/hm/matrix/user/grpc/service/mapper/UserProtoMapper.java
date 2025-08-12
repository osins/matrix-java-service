package club.hm.matrix.user.grpc.service.mapper;

import club.hm.homemart.club.shared.common.Dates;
import club.hm.homemart.club.shared.common.Objects;
import club.hm.matrix.user.data.entity.User;
import club.hm.matrix.user.grpc.service.UserOuterClass;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserProtoMapper implements IProtoMapper<UserOuterClass.User, User>{
    @Override
    public UserOuterClass.User to(User user) {
        return Optional.ofNullable(user).map(u -> {
            var builder = UserOuterClass.User.newBuilder();

            Objects.set(u.getId(), builder::setId);
            Objects.set(u.getUserId(), builder::setUserId);;
            Objects.set(u.getUsername(), builder::setUsername);;
            Objects.set(u.getPassword(), builder::setPassword);;
            Objects.set(u.getEmail(), builder::setEmail);;
            Objects.set(u.getDisplayName(), builder::setDisplayName);;
            Objects.set(u.getAvatarUrl(), builder::setAvatarUrl);;
            Objects.set(u.getIsActive(), builder::setIsActive);;
            Dates.set(u.getCreatedAt(), builder::setCreatedAt);
            Dates.set(u.getLastLoginAt(), builder::setLastLoginAt);

            return builder.build();
        }).orElse(null);
    }

    @Override
    public User from(UserOuterClass.User proto) {
        return Optional.ofNullable(proto).map(p -> {
            var user = new User();

            user.setId(p.getId());
            user.setUserId(p.getUserId());
            user.setUsername(p.getUsername());
            user.setPassword(p.getPassword());
            user.setEmail(p.getEmail());
            user.setDisplayName(p.getDisplayName());
            user.setAvatarUrl(p.getAvatarUrl());
            user.setIsActive(p.getIsActive());

            Dates.set(p.getCreatedAt(), user::setCreatedAt);
            Dates.set(p.getLastLoginAt(), user::setLastLoginAt);

            return user;
        }).orElse(null);
    }
}
