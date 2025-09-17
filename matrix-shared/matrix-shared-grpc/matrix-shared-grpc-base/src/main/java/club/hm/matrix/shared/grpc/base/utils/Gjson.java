package club.hm.matrix.shared.grpc.base.utils;

import com.google.protobuf.Message;

public final class Gjson {
    public static <T extends Message> String toJSONString(T value) {
        try {
            return value.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
