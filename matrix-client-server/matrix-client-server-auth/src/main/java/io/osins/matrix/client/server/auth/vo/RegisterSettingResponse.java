package io.osins.matrix.client.server.auth.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 注册设置 DTO，用于返回客户端注册时的配置信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterSettingResponse {
    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("client_name#en-US")
    private String clientNameEnUS;

    @JsonProperty("client_name#en-GB")
    private String clientNameEnGB;

    @JsonProperty("client_name#fr")
    private String clientNameFr;

    @JsonProperty("tos_uri")
    private String tosUri;

    @JsonProperty("tos_uri#fr")
    private String tosUriFr;

    @JsonProperty("policy_uri")
    private String policyUri;

    @JsonProperty("policy_uri#fr")
    private String policyUriFr;
}