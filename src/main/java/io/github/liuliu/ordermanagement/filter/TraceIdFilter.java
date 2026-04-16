package io.github.liuliu.ordermanagement.filter;

import io.github.liuliu.ordermanagement.context.TraceIdContext;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter to ensure every HTTP response includes the X-Trace-Id header.
 */
@Component
public class TraceIdFilter implements Filter {

    private static final String TRACE_HEADER = "X-Trace-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (response instanceof HttpServletResponse httpResponse) {
            String traceId = TraceIdContext.get();
            httpResponse.setHeader(TRACE_HEADER, traceId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            // Clear context to prevent memory leaks in thread pools
            TraceIdContext.clear();
        }
    }
}
