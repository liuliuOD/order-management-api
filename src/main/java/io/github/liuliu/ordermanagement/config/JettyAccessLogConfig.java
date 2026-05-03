package io.github.liuliu.ordermanagement.config;

import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.RequestLogWriter;
import org.springframework.boot.jetty.servlet.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyAccessLogConfig {

    @Bean
    WebServerFactoryCustomizer<JettyServletWebServerFactory> jettyAccessLogCustomizer() {
        return factory -> factory.addServerCustomizers(server -> {
            RequestLogWriter writer = new RequestLogWriter();
            writer.setAppend(true);

            CustomRequestLog requestLog = new CustomRequestLog(
                    writer,
                    "%{client}a - %u %{yyyy-MM-dd'T'HH:mm:ss.SSS'Z'|UTC}t \"%r\" %s %O \"%{Referer}i\" \"%{User-Agent}i\" duration_ms=%{ms}T request_id=%{requestId}attr"
            );

            server.setRequestLog(requestLog);
        });
    }
}
