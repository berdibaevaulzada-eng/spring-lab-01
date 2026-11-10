
package kz.iitu.springlab;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OptimisticLockingTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldDetectOptimisticLockingConflict() {

        Long bookId = bookRepository.findAll().get(0).id();

        EntityManager em1 = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        try {
            em1.getTransaction().begin();
            em2.getTransaction().begin();

            Book first = em1.find(Book.class, bookId);
            Book second = em2.find(Book.class, bookId);

            long originalVersion = first.getVersion();

            // First update
            first.setYear(first.year() + 1);
            em1.getTransaction().commit();

            System.out.println("First update successful");
            System.out.println("Original version: " + originalVersion);

            // Second update uses the old version
            second.setYear(second.year() + 2);

            Exception conflict = assertThrows(
                    Exception.class,
                    () -> {
                        em2.flush();
                        em2.getTransaction().commit();
                    }
            );

            System.out.println("Second update failed: "
                    + conflict.getClass().getSimpleName());

            System.out.println("Optimistic locking conflict detected");

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
