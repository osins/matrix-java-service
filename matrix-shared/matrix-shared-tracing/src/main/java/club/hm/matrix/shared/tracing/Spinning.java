package club.hm.matrix.shared.tracing;

import club.hm.matrix.shared.tracing.utils.Tracing;
import io.micrometer.observation.Observation;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.TracingObservationHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Spinning {
    private Observation.Context context;
    private Tracer tracer;

    public Spinning(Observation.Context context) {
        this.context = context;
        this.tracer = context.get(Tracer.class);
    }

    public static Spinning builder(Observation.Context context) {
        return new Spinning(context);
    }

    public static Span currentSpan(Observation.Context context, Tracer tracer) {
        return ((TracingObservationHandler<Observation.Context>) () -> tracer).getRequiredSpan(context);
    }

    public Spinning tracing() {
        Tracing.tracing(tracer);

        return this;
    }
}
