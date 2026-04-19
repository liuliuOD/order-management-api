package io.github.liuliu.ordermanagement.audit;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AuditRecord(
        String actor,
        String action,
        String targetType,
        String targetId,
        String result,
        String traceId,
        String beforeSnapshot,
        String afterSnapshot,
        String errorMessage,
        Instant occurredAt
) {
}
