package community.trace;

import community.trace.logtrace.LogTracer;

public abstract class LogTemplate<T> {

    private final LogTracer logTracer;

    public LogTemplate(LogTracer logTracer) {
        this.logTracer = logTracer;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = logTracer.begin(message);
            T result = call();
            logTracer.end(status);
            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }

    protected abstract T call();
}
