package io.github.liuliu.ordermanagement.config;

import io.github.liuliu.ordermanagement.interceptor.TracingAttributeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcTracingConfig implements WebMvcConfigurer {

    private final TracingAttributeInterceptor tracingAttributeInterceptor;

    public WebMvcTracingConfig(
            TracingAttributeInterceptor tracingAttributeInterceptor
    ) {
        this.tracingAttributeInterceptor = tracingAttributeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tracingAttributeInterceptor)
                .addPathPatterns("/**");
    }
}
