
package kz.iitu.springlab.catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(String author) {
        if (author == null || author.isBlank()) {
            return repository.findAll();
        }

        return repository.findAll().stream()
                .filter(book ->
                        book.author() != null &&
                                book.author().toLowerCase()
                                        .contains(author.toLowerCase()))
                .toList();
    }

    public Page<Book> find(Genre genre, Pageable pageable) {
        if (genre == null) {
            return repository.findAll(pageable);
        }

        return repository.findByGenre(genre, pageable);
    }

    public Optional<Book> findById(long id) {
        return Optional.of(
                repository.findById(id)
                        .orElseThrow(() ->
                                new BookNotFoundException(id))
        );
    }

    public Book create(Book book) {
        boolean exists = repository.findAll().stream()
                .anyMatch(existing ->
                        existing.title().equalsIgnoreCase(book.title())
                                && existing.author() != null
                                && book.author() != null
                                && existing.author()
                                .equalsIgnoreCase(book.author())
                );

        if (exists) {
            throw new DuplicateTitleException(book.title());
        }

        return repository.save(book);
    }

    public Book update(long id, Book book) {
        Book existing = repository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(id));

        existing.setTitle(book.title());
        existing.setAuthor(book.author());
        existing.setYear(book.year());
        existing.setGenre(book.genre());

        return repository.save(existing);
    }

    public boolean delete(long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
    public String testOptimisticLocking(long id) {
        Book first = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        Book second = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // Simulate the first update
        first.setYear(first.year() + 1);
        repository.saveAndFlush(first);

        // Simulate the second update with an old version
        second.setYear(second.year() + 2);

        try {
            repository.saveAndFlush(second);
            return "Both updates succeeded";
        } catch (ObjectOptimisticLockingFailureException e) {
            return "409 Conflict: Optimistic locking failed";
        }
    }
}
