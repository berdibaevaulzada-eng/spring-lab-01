
package kz.iitu.springlab.catalog;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(String author) {
        if (author == null) {
            return repository.findAll();
        }

        return repository.findAll().stream()
                .filter(book -> book.author().toLowerCase()
                        .contains(author.toLowerCase()))
                .toList();
    }

    public Optional<Book> findById(long id) {
        return Optional.of(
                repository.findById(id)
                        .orElseThrow(() -> new BookNotFoundException(id))
        );
    }

    public Book create(Book book) {
        boolean exists = repository.findAll().stream()
                .anyMatch(existing ->
                        existing.title().equalsIgnoreCase(book.title())
                                && existing.author().equalsIgnoreCase(book.author())
                );

        if (exists) {
            throw new DuplicateTitleException(book.title());
        }

        return repository.save(book);
    }
    public Book update(long id, Book book) {
        Book existing = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        Book updated = new Book(
                existing.id(),
                book.title(),
                book.author(),
                book.year(),
                book.genre(),
                existing.createdAt()
        );

        return repository.save(updated);
    }

    public boolean delete(long id) {
        return repository.deleteById(id);
    }
}
