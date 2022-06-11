package community.config;

import community.app.controller.IPostingController;
import community.app.controller.PostingController;
import community.app.service.PostingService;
import community.trace.LogHandler;
import community.trace.logtrace.LogTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyConfig {

    private final PostingController postingController;
    private final LogTracer logTracer;

    @Autowired
    public DynamicProxyConfig(PostingController postingController, LogTracer logTracer) {
        this.postingController = postingController;
        this.logTracer = logTracer;
    }

    @Bean
    IPostingController postingProxyController() {

        return (IPostingController) Proxy.newProxyInstance(IPostingController.class.getClassLoader(), new Class[] {IPostingController.class}, new LogHandler(postingController, logTracer));
    }
}
