package club.hm.matrix.auth.api.service;

import club.hm.matrix.auth.grpc.User;

public interface TokenService<R> {
    R generateToken(User user);
}
