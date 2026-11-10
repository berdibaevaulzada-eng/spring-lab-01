package kz.iitu.springlab.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final BookRepository bookRepository;

    public DataSeeder(
            AuthorRepository authorRepository,
            TagRepository tagRepository,
            BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {

        if (bookRepository.count() > 0) {
            return;
        }

        Tag fiction = tagRepository.save(new Tag("Fiction"));
        Tag classic = tagRepository.save(new Tag("Classic"));
        Tag fantasy = tagRepository.save(new Tag("Fantasy"));
        Tag science = tagRepository.save(new Tag("Science"));

        Author rowling = new Author("J. K. Rowling", "UK");
        Author orwell = new Author("George Orwell", "UK");
        Author hawking = new Author("Stephen Hawking", "UK");

        Book b1 = new Book("Harry Potter 1", "J. K. Rowling", 1997, Genre.FANTASY);
        Book b2 = new Book("Harry Potter 2", "J. K. Rowling", 1998, Genre.FANTASY);
        Book b3 = new Book("Harry Potter 3", "J. K. Rowling", 1999, Genre.FANTASY);

        Book b4 = new Book("1984", "George Orwell", 1949, Genre.FICTION);
        Book b5 = new Book("Animal Farm", "George Orwell", 1945, Genre.FICTION);
        Book b6 = new Book("Homage to Catalonia", "George Orwell", 1938, Genre.HISTORY);

        Book b7 = new Book("A Brief History of Time", "Stephen Hawking", 1988, Genre.SCIENCE);
        Book b8 = new Book("The Universe in a Nutshell", "Stephen Hawking", 2001, Genre.SCIENCE);

        rowling.addBook(b1);
        rowling.addBook(b2);
        rowling.addBook(b3);

        orwell.addBook(b4);
        orwell.addBook(b5);
        orwell.addBook(b6);

        hawking.addBook(b7);
        hawking.addBook(b8);

        b1.addTag(fantasy);
        b1.addTag(fiction);
        b2.addTag(fantasy);
        b3.addTag(fantasy);
        b4.addTag(classic);
        b4.addTag(fiction);
        b5.addTag(classic);
        b6.addTag(classic);
        b7.addTag(science);
        b8.addTag(science);

        authorRepository.save(rowling);
        authorRepository.save(orwell);
        authorRepository.save(hawking);

        System.out.println("Seed data inserted successfully!");
    }
}