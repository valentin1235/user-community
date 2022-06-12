package community.interceptor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/postings/**/")
                .addPathPatterns("/comments/**/")
                .excludePathPatterns("/postings")
                .excludePathPatterns("/postings/{postingId}/detail");

        registry.addInterceptor(new ListingInterceptor())
                .addPathPatterns("/postings")
                .addPathPatterns("/postings/{postingId}/detail");
    }
}
