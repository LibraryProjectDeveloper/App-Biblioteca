package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.Book.BookRequest;
import com.WebBiblioteca.DTO.Book.BookResponse;
import com.WebBiblioteca.Model.Autor;
import com.WebBiblioteca.Model.Book;
import com.WebBiblioteca.Model.EstadoBook;
import com.WebBiblioteca.Repository.BookReposity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookReposity bookReposity;
    public BookService(BookReposity bookReposity) {
        this.bookReposity = bookReposity;
    }

    //listar libros
    public List<BookResponse> getBookList(EstadoBook estadoBook) {
        List<BookResponse> bookResponseList = bookReposity.findByEstado(estadoBook)
                .stream().
                map(book -> new BookResponse(
                        book.getCodeBook(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublicationDate(),
                        book.getPublisher(),
                        book.getCategory(),
                        book.getStockTotal(),
                        book.getEstado()
                )).collect(Collectors.toList());
        if (bookResponseList.isEmpty()) {
            throw new RuntimeException("No hay libros disponibles");
        }
        return bookResponseList;
    }

    public Book getBookById(Long id) {
        return bookReposity.findByCodeBook(id).orElse(null);
    }

    @Transactional
    public Book addBook(BookRequest book) {
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
        Set<Autor> autors = book.getListAutores().stream()
                .map(autorDTO -> {
                    Autor autor = new Autor();
                    autor.setNombres(autorDTO.getNombres());
                    autor.setApellidos(autorDTO.getApellidos());
                    autor.setNacionalidad(autorDTO.getNacionalidad());
                    autor.setFechaNacimiento(autorDTO.getFechaNacimiento());
                    autor.setGenero(autorDTO.getGenero());
                    return autor;
                })
                .collect(Collectors.toSet());
        newBook.setAutores(autors);
        return bookReposity.save(newBook);
    }

    public Book updateBook(Long id, BookRequest book) {
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
            if (book.getCategoria() != null) {
                bookToUpdate.setCategory(book.getCategoria());
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
}
