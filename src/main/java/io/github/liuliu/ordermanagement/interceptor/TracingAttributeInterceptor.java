package io.github.liuliu.ordermanagement.interceptor;

import io.github.liuliu.ordermanagement.filter.RequestIdFilter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class TracingAttributeInterceptor implements HandlerInterceptor {
    private static final String SPAN_ATTRIBUTE_REQUEST_ID = "app.request_id";

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        String requestId = MDC.get(RequestIdFilter.MDC_KEY);

        Span span = Span.current();
        SpanContext spanContext = span.getSpanContext();
        if (spanContext.isValid() && span.isRecording()) {
            span.setAttribute(SPAN_ATTRIBUTE_REQUEST_ID, requestId);
        } else {
            log.warn("Span missing in TracingAttributeInterceptor");
        }

        return true;
    }
}
