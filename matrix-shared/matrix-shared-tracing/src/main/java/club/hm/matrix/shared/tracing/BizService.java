package club.hm.matrix.shared.tracing;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class BizService {
    private final ObservationRegistry observationRegistry;

    public String doBiz(String input) {
        return Observation.createNotStarted("biz.process", observationRegistry)
                .lowCardinalityKeyValue("biz.op", "process")
                .observe(() -> input.toUpperCase());
    }
}