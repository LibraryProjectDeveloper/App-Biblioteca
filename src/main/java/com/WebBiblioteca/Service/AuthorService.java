package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.Autor.AuthorRequest;
import com.WebBiblioteca.DTO.Autor.AuthorResponse;
import com.WebBiblioteca.Exception.ResourceNotFoundException;
import com.WebBiblioteca.Model.Author;
import com.WebBiblioteca.Repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public List<AuthorResponse> getAllAuthors() {
        return authorRepository.findAll().
                stream()
                .map(author -> new AuthorResponse(
                        author.getIdAuthor(),
                        author.getNames(),
                        author.getLastname(),
                        author.getNationality(),
                        author.getBirthdate(),
                        author.getGender()
                ))
                .toList();
    }
    @Transactional
    public AuthorRequest updateAuthor(AuthorRequest author) {
        Author authorToUpdate = authorRepository.findByIdAuthor(author.getIdAuthor()).orElseThrow(() -> new ResourceNotFoundException("Author","id",author.getIdAuthor()));
        if (author.getNames() != null) {
            authorToUpdate.setNames(author.getNames());
        }
        if (author.getLastname() != null) {
            authorToUpdate.setLastname(author.getLastname());
        }
        if (author.getNationality() != null) {
            authorToUpdate.setNationality(author.getNationality());
        }
        if (author.getBirthdate() != null) {
            authorToUpdate.setBirthdate(author.getBirthdate());
        }
        if (author.getGender() != null) {
            authorToUpdate.setGender(author.getGender());
        }
        authorRepository.save(authorToUpdate);
        return author;
    }
    public void deleteAuthor(Long id){
        Author author = authorRepository.findByIdAuthor(id).orElseThrow(()->new ResourceNotFoundException("Author","id",id));
        authorRepository.deleteById(id);
    }
}
