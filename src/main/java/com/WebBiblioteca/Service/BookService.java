package com.WebBiblioteca.Service;

import com.WebBiblioteca.BookDTO;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.EstadoBook;
import com.WebBiblioteca.Repository.BookReposity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookReposity bookReposity;
    public BookService(BookReposity bookReposity) {
        this.bookReposity = bookReposity;
    }

    //listar libros
    public List<Book> getBookList() {
        return bookReposity.findByEstado(EstadoBook.ACTIVO);
    }

    public Book getBookById(Long id) {
        return bookReposity.findByCodeBook(id).orElse(null);
    }

    public Book addBook(BookDTO book) {
        if (bookReposity.findByIsbn(book.getIsbn()).isPresent()) {
            return null;
        }
        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setPublisher(book.getPublisher());
        newBook.setPublicationDate(book.getPublicationDate());
        newBook.setCategory(book.getCategoria());
        newBook.setStockTotal(book.getStockTotal());
        newBook.setIsbn(book.getIsbn());
        newBook.setEstado(EstadoBook.ACTIVO);
        return bookReposity.save(newBook);
    }

    public Book updateBook(Long id, @Valid Book book) {
        Book bookToUpdate = bookReposity.findByCodeBook(id).orElse(null);
        if (bookToUpdate != null) {
            if (book.getTitle() != null) {
                bookToUpdate.setTitle(book.getTitle());
            }
            if (book.getPublisher() != null) {
                bookToUpdate.setPublisher(book.getPublisher());
            }
            if (book.getPublicationDate() != null) {
                bookToUpdate.setPublicationDate(book.getPublicationDate());
            }
            if (book.getCategory() != null) {
                bookToUpdate.setCategory(book.getCategory());
            }
            if (book.getStockTotal() != null) {
                bookToUpdate.setStockTotal(book.getStockTotal());
            }
            if (book.getIsbn() != null) {
                bookToUpdate.setIsbn(book.getIsbn());
            }
            if (book.getEstado() != null) {
                bookToUpdate.setEstado(book.getEstado());
            }
            return bookReposity.save(bookToUpdate);
        }
        return null;
    }

    /*
    public boolean deleteBook(Long id){
        return bookReposity.deleteBook(id);
    }
    public Book getBookById(Long id){
        return bookReposity.getBookById(id);
    }

     */
}
