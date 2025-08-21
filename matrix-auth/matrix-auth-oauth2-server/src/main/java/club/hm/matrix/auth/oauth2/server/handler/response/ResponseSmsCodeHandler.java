package club.hm.matrix.auth.oauth2.server.handler.response;

import club.hm.homemart.club.shared.common.uitls.Result;
import club.hm.matrix.shared.sms.aliyun.service.IAliyunSmsService;
import club.hm.matrix.auth.oauth2.server.enums.ResponseType;
import club.hm.matrix.auth.oauth2.server.service.SmsLoginCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponseSmsCodeHandler implements IResponseTypeHandler<Result<Void>> {
    private final SmsLoginCodeService smsLoginCodeService;
    private final IAliyunSmsService aliyunSmsService;

    @Override
    public ResponseType responseType() {
        return ResponseType.SMS_CODE;
    }

    @Override
    public Mono<Result<Void>> handle(ServerHttpRequest request) {
        var mobile = request.getQueryParams().getFirst("mobile");
        log.debug("handle sms code: {}", mobile);
        return smsLoginCodeService.getCode(mobile)
                // 如果缓存中已有验证码，直接提示用户
                .flatMap(code -> Mono.just(Result.success()))
                // 如果缓存中没有验证码，则生成并发送
                .switchIfEmpty(
                        smsLoginCodeService.generate(mobile)
                                .flatMap(code ->
                                        aliyunSmsService.sendLoginSms(mobile, code)
                                                .thenReturn(Result.success())
                                )
                );
    }
}
