package club.hm.matrix.auth.oauth2.server.controller;

import club.hm.matrix.auth.oauth2.server.service.GrantTypeAuthorizeService;
import club.hm.matrix.auth.oauth2.server.service.ResponseTypeAuthorizeService;
import com.fxg.module.common.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/authorize/oauth2/v1")
@RequiredArgsConstructor
public class AuthorizeController {
    private final GrantTypeAuthorizeService grantTypeAuthorizeService;
    private final ResponseTypeAuthorizeService responseTypeAuthorizeService;

    private Mono<?> authorizeResult(ServerHttpRequest request){
        if(request.getQueryParams().containsKey("response_type"))
            return responseTypeAuthorizeService.get(request);

        return grantTypeAuthorizeService.get(request);
    }

    // 授权端点 - WebFlux版本
    @GetMapping("/authorize")
    public Mono<Result<?>> authorize(ServerHttpRequest request) {
        return authorizeResult(request)
                .doOnSuccess(res -> log.debug("授权成功: {}, 结果: {}", request, res))
                .doOnError(e -> log.error("授权失败: {}, {}", request, e.getMessage(), e))
                .map(Result::success);
    }

    // 短信验证码发送 - WebFlux版本
//    @PostMapping("/authorize/sms_code")
//    public Mono<Result<Void>> authorizeSms(@RequestBody OAuth2SendSmsCodeRequest request) {
//        return Mono.fromCallable(() -> Optional.ofNullable(request.getMobile())
//                        .orElseThrow(() -> new RuntimeException("手机号码不能为空")))
//                .flatMap(mobile ->
//                        smsLoginCodeCache.getCodeAsync(mobile)
//                                .flatMap(existingCode -> {
//                                    if (existingCode.isPresent()) {
//                                        return Mono.just(Result.failure("短信验证码已发送,请不要重复请求"));
//                                    }
//
//                                    return smsLoginCodeCache.generateAsync(mobile)
//                                            .flatMap(codeOpt -> {
//                                                if (codeOpt.isPresent()) {
//                                                    return aliyunSmsService.sendLoginSmsAsync(mobile, codeOpt.get());
//                                                }
//                                                return Mono.just(Result.failure("发送失败"));
//                                            });
//                                })
//                )
//                .onErrorReturn(Result.failure("发送失败"));
//    }

    // 重置密码 - WebFlux版本
//    @PostMapping("/authorize/password/forgot")
//    public Mono<Result<Void>> resetPassword(@RequestBody PasswordForgotRequest request) {
//        return smsLoginCodeCache.getCodeAsync(request.getMobile())
//                .flatMap(codeOpt -> {
//                    if (!codeOpt.filter(code -> code.equalsIgnoreCase(request.getCode())).isPresent()) {
//                        return Mono.just(Result.failure("验证码错误"));
//                    }
//
//                    return securityUserService.forgotPasswordAsync(request);
//                })
//                .onErrorReturn(Result.failure("重置密码失败"));
//    }

    // 设备授权端点 - WebFlux版本
    @PostMapping("/device_authorization")
    public Mono<ResponseEntity<String>> deviceAuthorization(@RequestBody Map<String, String> requestBody) {
        return Mono.fromCallable(() -> {
                    // 实现设备授权逻辑，例如生成设备码、用户码等
                    return ResponseEntity.ok("Device authorization endpoint");
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Device authorization failed"));
    }

    // 设备验证端点 - WebFlux版本
    @GetMapping("/device_verification")
    public Mono<ResponseEntity<String>> deviceVerification(@RequestParam Map<String, String> params) {
        return Mono.fromCallable(() -> {
                    // 实现设备验证逻辑，例如验证用户码、设备码等
                    return ResponseEntity.ok("Device verification endpoint");
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Device verification failed"));
    }

    // 令牌自省端点 - WebFlux版本
    @PostMapping("/introspect")
    public Mono<ResponseEntity<Map<String, Object>>> introspect(@RequestBody Map<String, String> requestBody) {
        return Mono.fromCallable(() -> {
                    // 实现令牌自省逻辑，例如验证令牌的有效性、过期时间等
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Token introspection endpoint");
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Token introspection failed")));
    }

    // 令牌撤销端点 - WebFlux版本
    @PostMapping("/revoke")
    public Mono<ResponseEntity<String>> revoke(@RequestBody Map<String, String> requestBody) {
        return Mono.fromCallable(() -> {
                    // 实现令牌撤销逻辑，例如标记令牌为已撤销等
                    return ResponseEntity.ok("Token revocation endpoint");
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Token revocation failed"));
    }

    // JWK 集端点 - WebFlux版本
    @GetMapping("/jwks")
    public Mono<ResponseEntity<Map<String, Object>>> jwks() {
        return Mono.fromCallable(() -> {
                    // 实现 JWK 集返回逻辑，例如返回公钥信息等
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "JWK set endpoint");
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "JWK set retrieval failed")));
    }

    // OIDC 注销端点 - WebFlux版本
    @PostMapping("/logout")
    public Mono<ResponseEntity<String>> oidcLogout(ServerHttpRequest request) {
        return Mono.fromCallable(() -> {
                    // 实现OIDC注销逻辑
                    return ResponseEntity.ok("OIDC logout endpoint");
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("OIDC logout failed"));
    }

    // OIDC 用户信息端点 - WebFlux版本
    @GetMapping("/user/detail")
    public Mono<ResponseEntity<Map<String, Object>>> oidcUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        return Mono.fromCallable(() -> {
                    // 实现 OIDC 用户信息获取逻辑，例如根据访问令牌获取用户信息等
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "OIDC user info endpoint");
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "User info retrieval failed")));
    }

    // OIDC 客户端注册端点 - WebFlux版本
//    @PostMapping("/register")
//    public Mono<ResponseEntity<Result<TokenResponse>>> oidcClientRegistration(@RequestBody RegisterRequest data) {
//        return Mono.fromCallable(() -> {
//                    // 实现客户端注册逻辑
//                    return ResponseEntity.ok(Result.success(new TokenResponse()));
//                })
//                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(Result.error("Client registration failed")));
//    }
}