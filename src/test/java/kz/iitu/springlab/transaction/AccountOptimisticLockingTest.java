package kz.iitu.springlab.transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.persistence.RollbackException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountOptimisticLockingTest {

    @Autowired
    private EntityManagerFactory factory;

    @Test
    void testOptimisticLocking() {

        EntityManager em1 = factory.createEntityManager();
        EntityManager em2 = factory.createEntityManager();

        try {
            // Создаём два независимых контекста
            em1.getTransaction().begin();
            em2.getTransaction().begin();

            // Загружаем один счёт два раза
            Account first = em1.find(Account.class, 1L);
            Account second = em2.find(Account.class, 1L);

            assertNotNull(first, "Account 1 not found");
            assertNotNull(second, "Account 1 not found");

            System.out.println("Original version: " + first.getVersion());

            // Первая транзакция меняет баланс
            first.setBalance(
                    first.getBalance().add(new BigDecimal("10.00"))
            );

            em1.getTransaction().commit();
            System.out.println("First update successful");

            // Вторая транзакция использует старую версию
            second.setBalance(
                    second.getBalance().add(new BigDecimal("20.00"))
            );

            // Ожидаем конфликт версий
            RollbackException exception = assertThrows(
                    RollbackException.class,
                    () -> em2.getTransaction().commit()
            );

            assertInstanceOf(
                    OptimisticLockException.class,
                    exception.getCause()
            );

            System.out.println("Optimistic locking conflict detected!");

            System.out.println("Optimistic locking conflict detected");
            System.out.println("Second update rejected");

        } finally {
            if (em1.getTransaction().isActive()) {
                em1.getTransaction().rollback();
            }

            if (em2.getTransaction().isActive()) {
                em2.getTransaction().rollback();
            }

            em1.close();
            em2.close();
        }
    }
}
