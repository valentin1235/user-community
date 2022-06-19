package community.config;

import community.advice.LogTraceAdvice;
import community.trace.logtrace.LogTracer;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ProxyConfig {

    @Bean
    public Advisor advisor1() {
        List<Integer> a = new ArrayList<>();

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* community.app.controller..get*(..))");

        LogTraceAdvice advice = new LogTraceAdvice(new LogTracer());

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
