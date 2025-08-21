package club.hm.matrix.shared.grpc.base.utils;

public final class Gjson {
    public static <T extends com.google.protobuf.GeneratedMessageV3> String toJSONString(T value) {
        try {
            return value.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
