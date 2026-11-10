package kz.iitu.springlab.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditRecordRepository repository;

    public AuditService(AuditRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRequired(String message) {
        repository.saveAndFlush(new AuditRecord(message));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRequiresNew(String message) {
        repository.saveAndFlush(new AuditRecord(message));
    }
}
