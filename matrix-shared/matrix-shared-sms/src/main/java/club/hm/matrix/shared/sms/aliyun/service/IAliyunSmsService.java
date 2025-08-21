package club.hm.matrix.shared.sms.aliyun.service;

import club.hm.homemart.club.shared.common.uitls.Result;
import reactor.core.publisher.Mono;

public interface IAliyunSmsService {
    Mono<Result<Void>> sendLoginSms(String mobile, String code);
    Mono<Result<Void>> sendRegisterSms(String mobile, String code);;
}
