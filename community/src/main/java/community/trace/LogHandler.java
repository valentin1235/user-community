package community.trace;

import community.trace.logtrace.LogTracer;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class LogHandler implements InvocationHandler {

    private final Object target;
    private final LogTracer logTracer;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TraceStatus status = null;
        try {
            status = logTracer.begin("  @@@@@@@@@@@@@@  " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()");
            Object result = method.invoke(target, args);
            logTracer.end(status);

            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }
}
