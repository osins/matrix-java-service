package osins.matrix.auth.api.service;

import osins.matrix.auth.grpc.User;

public interface TokenService<R> {
    R generateToken(User user);
    R refreshToken(String refreshToken);
}
