package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookPageController {

	private final BookService service;

	public BookPageController(BookService service) {
		this.service = service;
	}

	@GetMapping
	public String list(
			@RequestParam(required = false) String author,
			Model model) {

		model.addAttribute("books", service.findAll(author));
		model.addAttribute("author", author);
		model.addAttribute("title", "Catalogue");

		return "books/list";
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id) {
		if (service.delete(id)) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/page")
	public ResponseEntity<List<Book>> page(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		List<Book> allBooks = service.findAll(null);

		int from = page * size;
		int to = Math.min(from + size, allBooks.size());

		List<Book> result =
				from >= allBooks.size()
						? List.of()
						: allBooks.subList(from, to);

		return ResponseEntity.ok()
				.header("X-Total-Count", String.valueOf(allBooks.size()))
				.body(result);
	}
}