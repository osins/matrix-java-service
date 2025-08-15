package club.hm.homemart.club.shared.common;

public final class Integers {
    public static boolean isZero(Integer value){
        return value == null || value == 0;
    }

    public static boolean isNoneZero(Integer value){
        return value != null && value > 0;
    }
}
