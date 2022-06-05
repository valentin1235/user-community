package community.trace;

import community.trace.logtrace.LogTracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogContext {

    private final LogTracer logTracer;

    public <T> T execute(String message, ILogStrategy<T> strategy) {
        TraceStatus status = null;
        try {
            status = logTracer.begin(message);
            T result = strategy.call();
            logTracer.end(status);
            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }
}
