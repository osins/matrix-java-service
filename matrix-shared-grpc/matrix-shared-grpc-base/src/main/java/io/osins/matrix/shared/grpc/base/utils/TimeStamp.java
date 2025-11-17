package io.osins.matrix.shared.grpc.base.utils;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class TimeStamp {
    private TimeStamp() {
    }

    public static Timestamp now(){
        return toTimestamp(LocalDateTime.now());
    }

    public static Timestamp toTimestamp(LocalDateTime ldt) {
        if(ldt==null)
            return null;
        return Timestamp.newBuilder()
                .setSeconds(ldt.atZone(ZoneId.systemDefault()).toEpochSecond())
                .setNanos(ldt.getNano())
                .build();
    }

    /** Protobuf Timestamp -> LocalDateTime */
    public static LocalDateTime fromTimestamp(Timestamp ts) {
        if (ts == null) {
            return null;
        }

        var instant = Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
