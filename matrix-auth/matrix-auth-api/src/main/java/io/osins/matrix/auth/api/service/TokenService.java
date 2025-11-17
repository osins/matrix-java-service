package io.osins.matrix.auth.api.service;

import io.osins.matrix.auth.grpc.User;

public interface TokenService<R> {
    R generateToken(User user);
    R refreshToken(String refreshToken);
}
