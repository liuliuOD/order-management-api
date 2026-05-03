package io.github.liuliu.ordermanagement.audit;

import java.util.HashMap;
import java.util.Map;

public final class AuditContextHolder {

    private static final ThreadLocal<Map<String, Object>> CONTEXT =
            ThreadLocal.withInitial(HashMap::new);

    private AuditContextHolder() {
    }

    public static void put(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    public static Object get(String key) {
        return CONTEXT.get().get(key);
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static Object getTargetId() {
        return get(AuditContextKey.TARGET_ID.getKey());
    }

    public static void setTargetId(String targetId) {
        put(AuditContextKey.TARGET_ID.getKey(), targetId);
    }

    public static Object getBeforeSnapshot() {
        return get(AuditContextKey.BEFORE_SNAPSHOT.getKey());
    }

    public static void setBeforeSnapshot(Object beforeSnapshot) {
        put(AuditContextKey.BEFORE_SNAPSHOT.getKey(), beforeSnapshot);
    }

    public static Object getAfterSnapshot() {
        return get(AuditContextKey.AFTER_SNAPSHOT.getKey());
    }

    public static void setAfterSnapshot(Object afterSnapshot) {
        put(AuditContextKey.AFTER_SNAPSHOT.getKey(), afterSnapshot);
    }
}

