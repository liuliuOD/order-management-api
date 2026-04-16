package io.github.liuliu.ordermanagement.context;

import java.util.UUID;

/**
 * A context to store and manage the traceId for the current thread.
 * 
 * Note on Virtual Threads (Project Loom):
 * In a Virtual Thread environment, each request is typically handled by a new virtual thread.
 * ThreadLocal is safe to use with virtual threads, as each virtual thread has its own 
 * copy of thread-local variables.
 */
public class TraceIdContext {
    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

    public static String get() {
        if (TRACE_ID.get() == null) {
            TRACE_ID.set(UUID.randomUUID().toString());
        }
        return TRACE_ID.get();
    }

    public static void clear() {
        TRACE_ID.remove();
    }
}
