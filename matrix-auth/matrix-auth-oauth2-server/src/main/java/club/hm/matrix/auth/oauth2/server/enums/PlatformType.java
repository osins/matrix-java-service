package club.hm.matrix.auth.oauth2.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatformType {
    /**
     * 微信小程序
     */
    WECHAT_MINI_APP(0),
    /**
     * 公众号
     */
    WECHAT_MP(1),
    /**
     * PC端
     */
    PC(2),
    /**
     * 移动端端h5
     */
    H5(3),
    /**
     * IOS
     */
    IOS(4),
    /**
     * ANDROID
     */
    ANDROID(5),
    /**
     * HARMONY
     */
    HARMONY(6);

    private final Integer value;
}
