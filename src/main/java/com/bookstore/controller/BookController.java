package com.bookstore.controller;

import com.bookstore.dto.BookRequest;
import com.bookstore.dto.BookResponse;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        BookResponse bookResponse = bookService.createBook(bookRequest);
        return new ResponseEntity<>(bookResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse bookResponse = bookService.getBookById(id);
        return ResponseEntity.ok(bookResponse);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest bookRequest) {
        BookResponse bookResponse = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(bookResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooksByTitle(@RequestParam String title) {
        List<BookResponse> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(@PathVariable String author) {
        List<BookResponse> books = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<BookResponse>> getBooksByCategory(@PathVariable String category) {
        List<BookResponse> books = bookService.getBooksByCategory(category);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
        BookResponse bookResponse = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(bookResponse);
    }
}
