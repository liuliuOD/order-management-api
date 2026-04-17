package io.github.liuliu.ordermanagement.config;

import io.github.liuliu.ordermanagement.filter.RequestIdFilter;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestInterceptor((request, body, execution) -> {
                    String requestId = MDC.get(RequestIdFilter.MDC_KEY);
                    if (requestId != null && !requestId.isBlank()) {
                        request.getHeaders().set(RequestIdFilter.HEADER_NAME, requestId);
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
