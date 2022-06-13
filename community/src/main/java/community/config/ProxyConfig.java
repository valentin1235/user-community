package community.config;

import community.advice.LogTraceAdvice;
import community.postprocessor.LogTracePostProcessor;
import community.trace.logtrace.LogTracer;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {

    @Bean
    public LogTracePostProcessor logTracePostProcessor() {
        return new LogTracePostProcessor(getAdvisor(new LogTracer()));
    }

    private Advisor getAdvisor(LogTracer logTracer) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("get*");

        LogTraceAdvice advice = new LogTraceAdvice(logTracer);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
