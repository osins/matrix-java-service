package io.osins.matrix.auth.security.constant;

public final class SecuritySettings {
    public final static String ROLE_PREFIX = "ROLE_"; // 默认值
    public final static String ROLE_ADMIN = "ADMIN";
    public final static String ROLE_USER = "USER";
    public final static String ROLE_ANONYMOUS = "ANONYMOUS";

    public static String getAuthority(String authority){
        return ROLE_PREFIX + authority;
    }
}
