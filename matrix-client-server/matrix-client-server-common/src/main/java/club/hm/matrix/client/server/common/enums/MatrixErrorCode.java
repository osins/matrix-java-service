/**
 * Matrix API 错误码枚举。
 * 只包含错误码本身，不直接包含多语言描述，
 * 描述建议通过 MessageSource / ResourceBundle 按 Locale 获取。
 */
package club.hm.matrix.client.server.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matrix API 错误码枚举。
 * 枚举名即错误码，例如 M_FORBIDDEN、M_UNKNOWN_TOKEN。
 */
public enum MatrixErrorCode {

    NONE,

    // —— 通用错误码 ——
    M_FORBIDDEN,
    M_UNKNOWN_TOKEN,
    M_MISSING_TOKEN,
    M_USER_LOCKED,
    M_USER_SUSPENDED,
    M_BAD_JSON,
    M_NOT_JSON,
    M_NOT_FOUND,
    M_LIMIT_EXCEEDED,
    M_UNRECOGNIZED,
    M_UNKNOWN,

    // —— 端点特定错误码 ——
    M_UNAUTHORIZED,
    M_USER_DEACTIVATED,
    M_USER_IN_USE,
    M_INVALID_USERNAME,
    M_ROOM_IN_USE,
    M_INVALID_ROOM_STATE,
    M_THREEPID_IN_USE,
    M_THREEPID_NOT_FOUND,
    M_THREEPID_AUTH_FAILED,
    M_THREEPID_DENIED,
    M_SERVER_NOT_TRUSTED,
    M_UNSUPPORTED_ROOM_VERSION,
    M_INCOMPATIBLE_ROOM_VERSION,
    M_BAD_STATE,
    M_GUEST_ACCESS_FORBIDDEN,
    M_CAPTCHA_NEEDED,
    M_CAPTCHA_INVALID,
    M_MISSING_PARAM,
    M_INVALID_PARAM,
    M_TOO_LARGE,
    M_EXCLUSIVE,
    M_RESOURCE_LIMIT_EXCEEDED,
    M_CANNOT_LEAVE_SERVER_NOTICE_ROOM,
    M_THREEPID_MEDIUM_NOT_SUPPORTED;

    /**
     * 让 Jackson 序列化时输出字符串形式（errcode）
     */
    @JsonValue
    public String toValue() {
        return this.name();
    }

    /**
     * 根据字符串解析枚举
     */
    @JsonCreator
    public static MatrixErrorCode fromValue(String value) {
        if (value == null) {
            return NONE;
        }
        try {
            return MatrixErrorCode.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return NONE; // 或者 M_UNKNOWN
        }
    }

    public boolean isNone() {
        return this == NONE;
    }
}
