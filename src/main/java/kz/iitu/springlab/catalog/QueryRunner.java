
package kz.iitu.springlab.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Profile("query-test")
public class QueryRunner implements CommandLineRunner {

    private final BookRepository repository;
    private final TransactionTemplate transactionTemplate;

    public QueryRunner(BookRepository repository,
                       TransactionTemplate transactionTemplate) {
        this.repository = repository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void run(String... args) {

        System.out.println("=== TASK 3.3 QUERY RESULTS ===");

        System.out.println("findByGenre: " +
                repository.findByGenre(Genre.FANTASY).size());

        System.out.println("findByTitleContainingIgnoreCase: " +
                repository.findByTitleContainingIgnoreCase("Harry").size());

        System.out.println("findByYearBetweenOrderByYearDesc: " +
                repository.findByYearBetweenOrderByYearDesc(1900, 2025).size());

        System.out.println("findTop3ByGenreOrderByYearDesc: " +
                repository.findTop3ByGenreOrderByYearDesc(Genre.FANTASY).size());

        System.out.println("countByAuthorEntity_Name: " +
                repository.countByAuthorEntity_Name("J. K. Rowling"));

        System.out.println("search (JPQL): " +
                repository.search(Genre.FANTASY, 1990).size());

        System.out.println("=== TASK 3.4 N+1 TEST ===");

        transactionTemplate.executeWithoutResult(status -> {

            System.out.println("=== WITHOUT JOIN FETCH ===");

            repository.findAll().forEach(book -> {
                System.out.println(book.title() + " - " +
                        book.getAuthorEntity().getName());
            });
        });

        transactionTemplate.executeWithoutResult(status -> {

            System.out.println("=== WITH JOIN FETCH ===");

            repository.findAllWithAuthor().forEach(book -> {
                System.out.println(book.title() + " - " +
                        book.getAuthorEntity().getName());
            });
        });
    }
}
