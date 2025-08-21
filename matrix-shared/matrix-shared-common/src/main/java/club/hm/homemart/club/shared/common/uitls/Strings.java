package club.hm.homemart.club.shared.common.uitls;

import club.hm.homemart.club.shared.common.Exception.CustomException;
import club.hm.homemart.club.shared.common.Exception.CustomeAsyncException;
import com.google.common.base.Joiner;

import java.util.Optional;
import java.util.function.Function;

/**
 * 字符串操作工具类，提供常用的字符串判空方法。
 */
public final class Strings {
    public static String join(Object... args){
        return Joiner.on(",").join(args);
    }

    /**
     * 检查指定字符串是否为null或空字符串。
     *
     * @param s 要检查的字符串
     * @return 如果字符串为null或空字符串则返回true，否则返回false
     */
    public static boolean isNullOrEmpty(String s) {
        return com.google.common.base.Strings.isNullOrEmpty(s);
    }

    /**
     * 检查指定字符串是否既不为null也不为空字符串。
     *
     * @param s 要检查的字符串
     * @return 如果字符串既不为null也不为空字符串则返回true，否则返回false
     */
    public static boolean isNoneNullOrEmpty(String s) {
        return !isNullOrEmpty(s);
    }

    public static Optional<String> fitler(String s) {
        return Optional.ofNullable(s)
                .filter(Strings::isNoneNullOrEmpty);
    }

    public static String getOrElse(String s, String defaultValue, String exceptionMessage) {
        var option = fitler(s);
        if (option.isPresent()) {
            return option.get();
        }

        if (defaultValue != null) {
            return defaultValue;
        }

        if (exceptionMessage != null)
            throw new CustomeAsyncException(exceptionMessage);

        return null;
    }

    public static String getOrDThrow(String t, String exceptionMessage) {
        return getOrElse(t, null, exceptionMessage);
    }

    public static String get(String t, String defaultValue) {
        return getOrElse(t, defaultValue, null);
    }

    public static String get(String t) {
        return getOrElse(t, null, null);
    }

    public static String map(String value, Function<String,String> mapper){
        return fitler(value)
                .map(mapper)
                .orElse( null);
    }

    public static String map(String value, Function<String,String> mapper, String exceptionMessage){
        return fitler(value)
                .map(mapper)
                .orElseThrow( ()-> new CustomException(exceptionMessage));
    }
}