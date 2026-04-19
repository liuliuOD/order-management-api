package io.github.liuliu.ordermanagement.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditService {
    public void record(AuditRecord record) {
        log.info("AUDIT record={}", record);
    }
}
