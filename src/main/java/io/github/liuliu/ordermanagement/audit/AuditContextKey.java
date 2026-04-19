package io.github.liuliu.ordermanagement.audit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuditContextKey {
    TARGET_ID("targetId"),
    BEFORE_SNAPSHOT("beforeSnapshot"),
    AFTER_SNAPSHOT("afterSnapshot");

    private final String key;
}

