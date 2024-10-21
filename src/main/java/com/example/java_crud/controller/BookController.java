package com.example.java_crud.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.java_crud.model.Book;
import com.example.java_crud.repo.BookRepo;
import com.example.java_crud.service.BookCoverService;

import jakarta.validation.Valid;


@Controller
public class BookController {
    @Autowired
    private BookRepo bookRepo;
    
    @Autowired
    private BookCoverService bookCoverService;

    @GetMapping("/h2")
    public String index() {
        return "index"; 
    }

    
    @ResponseBody
    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> bookList = new ArrayList<>();
            bookRepo.findAll().forEach(bookList::add);

            if (bookList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ResponseBody
    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> bookData = bookRepo.findById(id);

        if (bookData.isPresent()) {
            return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @ResponseBody   
    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        if (book.getRating() < 1 || book.getRating() > 5) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        try {
            // Fetch book cover before saving the book
            String coverUrl = bookCoverService.getBookCover(book.getTitle(), book.getAuthor());
            book.setCoverUrl(coverUrl);  // Set the cover URL
            Book bookObj = bookRepo.save(book);
            return new ResponseEntity<>(bookObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @ResponseBody
    @PostMapping("/updateBookById/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book newBookData) {
        Optional<Book> oldBookData = bookRepo.findById(id);

        if (oldBookData.isPresent()) {
            Book updatedBookData = oldBookData.get();
            updatedBookData.setTitle(newBookData.getTitle());
            updatedBookData.setAuthor(newBookData.getAuthor());
            
            if (newBookData.getRating() >= 1 && newBookData.getRating() <= 5) {
                updatedBookData.setRating(newBookData.getRating()); 
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
            }
            Book bookObj = bookRepo.save(updatedBookData);
            return new ResponseEntity<>(bookObj, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @ResponseBody
    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long id) {
        bookRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

