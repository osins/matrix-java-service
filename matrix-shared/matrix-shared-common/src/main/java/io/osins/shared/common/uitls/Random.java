package io.osins.shared.common.uitls;

import org.apache.commons.lang3.RandomStringUtils;

public final class Random {
    public static String number(int length) {
        return RandomStringUtils.secure().next(4, false, true);
    }

    public static String string(int length, boolean letters, boolean numbers) {
        return RandomStringUtils.secure().next(4, letters, numbers);
    }
}
