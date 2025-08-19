package club.hm.matrix.auth.oauth2.server.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ClientID {
    NONE("6857b966-f5c2-4322-9342-3bc4f2e99bd2", false, null, null),
    DEVELOPER("f40530d6-67b1-4f60-adb1-a31074c4bf34", false, "开发者", "QMYwAQwNbaFBAPWFWG812a711Ti552eFZzv7XfKKgrq2yWm2yrbQWfQ5h1dzpUYgzFReUxcC40HbVtZ1J68V"),
    PLATFORM("platform", false, "平台", "xL9x0AhuA4hhfy4evngzj2E7aBqbhi7xquX5J1PTZtD24RFv8WrxkZnWUK11FzNNV0CqRAk9rY49V0296Yhx"),
    SHOP("shop", false, "店铺", "AmaRxxAdgYJXhEopcvKFs86tJ34LF80XxkAqvpJ18Ma89XAZju1eLRyZRrgCdgMn9F19fLRdJj7amhQWdroQ"),
    CONSUMER("supplier", false, "消费者", "BgyLFFN8hkqvmzFupLtPCD82P7yRqmQ9Nv7Rq8qXkd809hcQg1n0a0fDYBFEFgBfCcHeC0uuJVNMuJUgghzM"),
    STORE("store", false, "仓库", "KKXoguuaj4TDKwNEV30M0v5bWdNUikGjE82yod86Dj5saTvpbP3hNoBo8tU3bRNQJvXoz1V8hdYAPprKemWE"),
    SUPPLIER("supplier", false, "供应商", "H5F2kjzffDMfYRYy6qYYj38tuo1hfPUKdpnczpyxJgd5emsnrudHR76JrJ2xZVEJkz19TgPdct93RDVq06rj"),
    CALCULATE("f40530d6-67b1-4f60-adb1-a31074c4bf34", false, "计算服务", "Cx40uH9YaXNMD7Yvpok5Tt1cb6Ca1GCsbjRRd3U7Jxbsuz0DrCesfc6ubDueL68HFnrRD6j45hMh0Nxc86MN"),
    LOW_CODES("8d5ebd62-2119-47c6-ae25-345666111c65", false, "低代码平台", "hN81ussgfJuH7u2YcWNriPk73zGwzxiVFeKPJg2Nmk2vUAXMAM3EmB7GX1ub0y9gtwwaB1MWHifNQ2zMHDD2"),
    DIGITAL_ASSETS("eebb2ccc-0a9f-4710-84e6-aa4132f0719c", true, "数字资产", "7VFmi9tJ6PgwTjgrXVZgRHbdT5kjYE4ePrQP8TauUQ8gQUuAMDhNkqnUL65957c9cqeqvi0CKYGRZon0PygY");

    private final String code;
    /**
     * 需要验证客户端
     */
    private final boolean needCode;
    private final String secret;
    private final String description;

    ClientID(String code, boolean needCode, String description, String secret) {
        this.code = code;
        this.needCode = needCode;
        this.secret = secret;
        this.description = description;
    }

    public static ClientID of(String code) {
        return Arrays.stream(values()).filter(client -> client.code.equals(code)).findFirst().orElse(NONE);
    }

    // 验证clientId
    public static boolean isClientIdInvalid(String clientId) {
        return clientId == null || Arrays.stream(values()).noneMatch(client -> client.code.equals(clientId));
    }

    public boolean isNone() {
        return NONE.equals(this);
    }

    /**
     * 不需要验证code的客户端
     */
    public static boolean isNeedCode(String clientId) {
        if (isClientIdInvalid(clientId)) {
            return true;
        }

        return Arrays.stream(values()).anyMatch(client -> client.needCode && client.code.equals(clientId));
    }

    public static boolean verifySecretInvalid(String clientId, String secret) {
        if (isClientIdInvalid(clientId)) {
            return true;
        }

        return Arrays.stream(values()).noneMatch(client -> client.code.equals(clientId) && client.secret.equals(secret));
    }
}
