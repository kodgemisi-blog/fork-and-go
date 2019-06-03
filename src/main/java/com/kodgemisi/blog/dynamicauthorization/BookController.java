package com.kodgemisi.blog.dynamicauthorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on May, 2019
 *
 * @author destan
 */
@Controller
@RequestMapping("/books")
class BookController {

	// For demonstrations purposes only, never use such thing.
	private final Map<String, Book> books = Collections.synchronizedMap(new HashMap<>());

	@PostConstruct
	private void init() {
		books.put("10001", new Book("10001", "Brave new world", "Aldous Huxley", "Lorem ipsum"));
		books.put("10002", new Book("10002", "1984", "George Orwell", "Dolor sit amed"));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('books:list')")
	String index(Model model) {

		model.addAttribute("books", books.values());
		return "books/index";
	}

	@GetMapping("/new")
	@PreAuthorize("hasAuthority('books:create')")
	String createForm(Model model) {

		model.addAttribute("book", new Book());
		return "books/create-form";
	}

	@GetMapping("/{isbn}")
	@PreAuthorize("hasAuthority('books:read')")
	String read(@PathVariable String isbn, Model model) {

		if (!books.containsKey(isbn)) {
			throw new ResourceNotFoundException();
		}

		model.addAttribute("book", books.get(isbn));
		return "books/read";
	}

	@PostMapping
	@PreAuthorize("hasAuthority('books:create')")
	String create(Book book) {

		books.put(book.getIsbn(), book);
		return "redirect:/books";
	}

	@GetMapping("/{isbn}/edit")
	@PreAuthorize("hasAuthority('books:edit')")
	String editForm(@PathVariable String isbn, Model model) {

		if (!books.containsKey(isbn)) {
			throw new ResourceNotFoundException();
		}

		model.addAttribute("book", books.get(isbn));
		return "books/edit-form";
	}

	@PutMapping("/{isbn}")
	@PreAuthorize("hasAuthority('books:edit')")
	String edit(Book book, @PathVariable String isbn) {

		if (!books.containsKey(isbn)) {
			throw new ResourceNotFoundException();
		}

		if (!isbn.equals(book.getIsbn())) {
			throw new IllegalStateException();
		}

		books.put(book.getIsbn(), book);
		return "redirect:/books";
	}

	@DeleteMapping("/{isbn}")
	@PreAuthorize("hasAuthority('books:delete')")
	String delete(@PathVariable String isbn) {

		if (!books.containsKey(isbn)) {
			throw new ResourceNotFoundException();
		}

		books.remove(isbn);
		return "redirect:/books";
	}

}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Book {

	private String isbn;

	private String title;

	private String author;

	private String content;

	@Override
	public int hashCode() {
		return isbn.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Book book = (Book) o;

		return isbn.equals(book.isbn);

	}
}
