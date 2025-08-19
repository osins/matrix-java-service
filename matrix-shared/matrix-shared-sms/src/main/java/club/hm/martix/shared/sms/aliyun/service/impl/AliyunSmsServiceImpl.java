package club.hm.martix.shared.sms.aliyun.service.impl;

import club.hm.martix.shared.sms.aliyun.param.AliyunSmsRequest;
import club.hm.martix.shared.sms.aliyun.service.AliyunSmsClient;
import club.hm.martix.shared.sms.aliyun.service.IAliyunSmsService;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxg.module.common.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunSmsServiceImpl implements IAliyunSmsService {
    private final AliyunSmsClient client;
    private final ObjectMapper objectMapper;

    private static SendSmsRequest createSmsRequest(){
        return new SendSmsRequest()
                .setSignName("钜博技术")
                .setTemplateCode("SMS_489405100");
    }

    private static SendSmsRequest createLoginSmsRequest(){
        return createSmsRequest().setTemplateCode("SMS_489405100");
    }

    private static SendSmsRequest createRegisterSmsRequest(){
        return createSmsRequest().setTemplateCode("SMS_489490074");
    }

    @Override
    public Result<Void> sendLoginSms(String mobile, String code){
        try{
            var response = client.sendSms(createLoginSmsRequest()
                    .setPhoneNumbers(mobile)
                    .setTemplateParam(objectMapper.writeValueAsString(AliyunSmsRequest.builder()
                            .code(code)
                            .build())));

            return Result.<Void>of("OK".equalsIgnoreCase(response.getBody().getCode()), "验证码发送成功", "验证码发送失败")
                    .ifFail(()->log.warn("阿里云短信发送异常: {}, {}, 返回信息: {}", mobile, code, response));
        } catch (Exception ex) {
            log.warn("阿里云短信模板创建异常: {}, {}, 错误: {}", mobile, code, ex.getMessage(), ex);
        }

        return null;
    }

    @Override
    public Result<Void> sendRegisterSms(String mobile, String code){
        try{
            var response = client.sendSms(createRegisterSmsRequest()
                    .setPhoneNumbers(mobile)
                    .setTemplateParam(objectMapper.writeValueAsString(AliyunSmsRequest.builder()
                            .code(code)
                            .build())));

            return Result.<Void>of("OK".equalsIgnoreCase(response.getBody().getCode()), "验证码发送成功", "验证码发送失败")
                    .ifFail(()->log.warn("阿里云短信发送异常: {}, {}, 返回信息: {}", mobile, code, response));
        } catch (Exception ex) {
            log.warn("阿里云短信模板创建异常: {}, {}, 错误: {}", mobile, code, ex.getMessage(), ex);
        }

        return null;
    }
}
