package io.github.liuliu.ordermanagement.audit;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(auditable)")
    public Object around(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String traceId = MDC.get("traceId");
        String actor = MDC.get("userId");

        try {
            Object result = joinPoint.proceed();

            auditService.record(
                    AuditRecord.builder()
                            .actor(actor != null ? actor : "anonymous")
                            .action(auditable.action().name())
                            .targetType(auditable.targetType().toString())
                            .targetId((String) AuditContextHolder.getTargetId())
                            .result("SUCCESS")
                            .traceId(traceId)
                            .beforeSnapshot(String.valueOf(AuditContextHolder.getBeforeSnapshot()))
                            .afterSnapshot(String.valueOf(
                                    AuditContextHolder.getAfterSnapshot() != null
                                            ? AuditContextHolder.getAfterSnapshot()
                                            : result
                            ))
                            .occurredAt(Instant.now())
                            .build()
            );

            return result;
        } catch (Throwable ex) {
            auditService.record(
                    AuditRecord.builder()
                            .actor(actor != null ? actor : "anonymous")
                            .action(auditable.action().name())
                            .targetType(auditable.targetType().toString())
                            .targetId((String) AuditContextHolder.getTargetId())
                            .result("FAILED")
                            .traceId(traceId)
                            .beforeSnapshot(String.valueOf(AuditContextHolder.getBeforeSnapshot()))
                            .errorMessage(ex.getMessage())
                            .occurredAt(Instant.now())
                            .build()
            );
            throw ex;
        } finally {
            AuditContextHolder.clear();
        }
    }
}
