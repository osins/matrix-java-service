package io.osins.shared.common.uitls;

public final class Integers {
    public static boolean isZero(Integer value){
        return value == null || value == 0;
    }

    public static boolean isNoneZero(Integer value){
        return value != null && value > 0;
    }
}
