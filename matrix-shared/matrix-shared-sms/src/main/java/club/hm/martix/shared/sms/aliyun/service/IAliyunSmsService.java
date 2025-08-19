package club.hm.martix.shared.sms.aliyun.service;

import com.fxg.module.common.Result;

public interface IAliyunSmsService {
    Result<Void> sendLoginSms(String mobile, String code);
    Result<Void> sendRegisterSms(String mobile, String code);
}
