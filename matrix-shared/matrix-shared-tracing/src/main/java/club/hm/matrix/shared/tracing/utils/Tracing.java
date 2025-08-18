package club.hm.matrix.shared.tracing.utils;

import io.micrometer.observation.Observation;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.TracingObservationHandler;
import org.slf4j.MDC;
import reactor.util.context.Context;

public final class Tracing {
    public static void tracing(Context context){
//        MDC.put("traceId", context.traceId());
//        MDC.put("spanId", context.spanId());
    }

    public static void tracing(Tracer tracer){
        try{
            var context = tracer.currentTraceContext().context();
            if(context == null)
                return;

            MDC.put("traceId", context.traceId());
            MDC.put("spanId", context.spanId());
        }catch (Exception ignored){

        }
    }


    public static void tracing(Observation.Context context, Tracer tracer){
        var span= ((TracingObservationHandler<Observation.Context>) () -> tracer).getRequiredSpan(context);
        MDC.put("traceId", span.context().traceId());
        MDC.put("spanId", span.context().spanId());
    }
}
