package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.Book;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BookReposity {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final HashMap<Long, Book> bookMap = new HashMap<>();

    public Map<Long,Book> getBookList(){
        return bookMap;
    }

    public Book getBookById(Long id){
        return bookMap.get(id);
    }

    public Book addBook(Book book){
        Long id = idGenerator.getAndIncrement();
        book.setCodeBook(id);
        bookMap.put(id, book);
        return book;
    }

    public Book updateBook(Long id, Book book){
       return bookMap.computeIfPresent(id, (key, value) -> {
           value.setTitle(book.getTitle());
           value.setTitle(book.getTitle());
           value.setPublisher(book.getPublisher());
           value.setPublicationDate(book.getPublicationDate());
           value.setCategory(book.getCategory());
           value.setStockTotal(book.getStockTotal());
           return value;
        });
    }
}
