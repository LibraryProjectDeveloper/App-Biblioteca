package com.WebBiblioteca.Service;

import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Repository.BookReposity;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class BookService {

    private final BookReposity bookReposity;
    public BookService(BookReposity bookReposity) {
        this.bookReposity = bookReposity;
    }

    public Map<Long, Book> getBookList() {
        return bookReposity.getBookList();
    }
    public Book addBook(Book book){
        if (book != null) {
            return bookReposity.addBook(book);
        } else {
            return null;
        }
    }
    public Book updateBook (Long id,Book book){
        Book bookToUpdate = bookReposity.getBookById(id);
        if(bookToUpdate !=null){
            if(book.getTitle() != null){
                bookToUpdate.setTitle(book.getTitle());
            }
            if(book.getPublisher() != null){
                bookToUpdate.setPublisher(book.getPublisher());
            }
            if(book.getPublicationDate() != null){
                bookToUpdate.setPublicationDate(book.getPublicationDate());
            }
            if(book.getCategory() != null){
                bookToUpdate.setCategory(book.getCategory());
            }
            if(book.getStockTotal() != null){
                bookToUpdate.setStockTotal(book.getStockTotal());
            }
            return bookReposity.updateBook(id, bookToUpdate);
        }else{
            return null;
        }
    }
    public boolean deleteBook(Long id){
        return bookReposity.deleteBook(id);
    }
}
