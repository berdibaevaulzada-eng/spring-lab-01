
package kz.iitu.springlab.web.dto;

import kz.iitu.springlab.catalog.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toDomain(BookRequest request) {
        return new Book(
                request.title(),
                request.author(),
                request.year(),
                request.genre()
        );
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(
                book.id(),
                book.title(),
                book.author(),
                book.year(),
                book.genre(),
                book.createdAt()
        );
    }
    public java.util.List<BookResponse> toResponseList(
            java.util.List<Book> books) {

        return books.stream()
                .map(this::toResponse)
                .toList();
    }
}
