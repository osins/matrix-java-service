package club.hm.matrix.auth.api.service;

import club.hm.matrix.auth.api.domain.User;

public interface TokenService<R> {
    R generateToken(User user);
    R refreshToken(String refreshToken);
}
