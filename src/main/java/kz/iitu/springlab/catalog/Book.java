
package kz.iitu.springlab.catalog;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    private String author;

    private int year;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Genre genre;

    private boolean archived;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    @Version
    private long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author authorEntity;

    @ManyToMany
    @JoinTable(
            name = "book_tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    protected Book() {
    }

    public Book(String title, String author, int year, Genre genre) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
    }

    public Book(Long id, String title, String author,
                int year, Genre genre, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
        this.createdAt = createdAt;
    }

    public Book(Long id, String title, String author, int year) {
        this(title, author, year, null);
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String author() {
        return author;
    }

    public int year() {
        return year;
    }

    public Genre genre() {
        return genre;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean isArchived() {
        return archived;
    }

    public long getVersion() {
        return version;
    }

    public Author getAuthorEntity() {
        return authorEntity;
    }

    public void setAuthor(Author authorEntity) {
        this.authorEntity = authorEntity;
        if (authorEntity != null) {
            this.author = authorEntity.getName();
        }
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
