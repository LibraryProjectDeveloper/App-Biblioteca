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
        book.setCodeBook(idGenerator.getAndIncrement());
        bookMap.put(book.getCodeBook(), book);
        return book;
    }

    public Book updateBook(Long id, Book book){
       if(bookMap.containsKey(id)){
           bookMap.put(id, book);
           return book;
       } else {
           return null;
       }
    }
    public boolean deleteBook(Long id){
        if(bookMap.containsKey(id)){
            bookMap.remove(id);
            return true;
        } else {
            return false;
        }
    }
}
