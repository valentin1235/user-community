package community.trace.logtrace;

import community.trace.TraceStatus;

public interface ILogTraceable {
    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}

