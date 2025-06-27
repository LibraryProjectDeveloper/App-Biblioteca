package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.Autor.AuthorRequest;
import com.WebBiblioteca.DTO.Autor.AuthorResponse;
import com.WebBiblioteca.Model.Author;
import com.WebBiblioteca.Model.Gender;
import com.WebBiblioteca.Repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors() {
        Author author = mock(Author.class);
        when(author.getIdAuthor()).thenReturn(1L);
        when(author.getNames()).thenReturn("Autor");
        when(author.getLastname()).thenReturn("Apellido");
        when(author.getNationality()).thenReturn("PE");
        when(author.getBirthdate()).thenReturn(null);
        when(author.getGender()).thenReturn(Gender.MASCULINO);
        when(authorRepository.findAll()).thenReturn(List.of(author));
        List<AuthorResponse> result = authorService.getAllAuthors();
        assertEquals(1, result.size());
        assertEquals("Autor", result.getFirst().getNames());
    }

    @Test
    void testUpdateAuthor() {
        AuthorRequest req = mock(AuthorRequest.class);
        when(req.getIdAuthor()).thenReturn(1L);
        Author author = new Author();
        when(authorRepository.findByIdAuthor(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        AuthorRequest result = authorService.updateAuthor(req, 1L);
        assertNotNull(result);
    }

    @Test
    void testDeleteAuthor() {
        Author author = new Author();
        when(authorRepository.findByIdAuthor(1L)).thenReturn(Optional.of(author));
        doNothing().when(authorRepository).deleteById(1L);
        assertDoesNotThrow(() -> authorService.deleteAuthor(1L));
    }
}

