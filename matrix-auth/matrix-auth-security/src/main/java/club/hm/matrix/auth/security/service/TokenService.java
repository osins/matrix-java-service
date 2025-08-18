package club.hm.matrix.auth.security.service;

import club.hm.matrix.auth.grpc.User;
import club.hm.matrix.auth.api.token.TokenResponse;

public interface TokenService {
    TokenResponse generateToken(User user);
}
