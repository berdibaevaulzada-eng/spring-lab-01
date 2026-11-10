package kz.iitu.springlab.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PropagationServiceTest {

    @Autowired
    private PropagationService service;

    @Autowired
    private AuditRecordRepository repository;

    @Test
    void testRequired() {
        repository.deleteAll();

        assertThrows(RuntimeException.class,
                () -> service.testRequired());

        assertEquals(0, repository.count());

        System.out.println("REQUIRED: audit record rolled back");
    }

    @Test
    void testRequiresNew() {
        repository.deleteAll();

        assertThrows(RuntimeException.class,
                () -> service.testRequiresNew());

        assertEquals(1, repository.count());

        System.out.println("REQUIRES_NEW: audit record saved");
    }
}
