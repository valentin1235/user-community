package community.advice;

import community.trace.TraceStatus;
import community.trace.logtrace.LogTracer;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class LogTraceAdvice implements MethodInterceptor {
    private final LogTracer logTracer;

    public LogTraceAdvice(LogTracer logTracer) {
        this.logTracer = logTracer;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TraceStatus status = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(invocation.getClass().getName());
            sb.append('.');
            sb.append(invocation.getMethod());
            status = logTracer.begin("  @@@@@@@@@@@@@@  " + sb);


            final Object result = invocation.proceed();
            System.out.println(result.getClass());

            logTracer.end(status);

            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }
}
