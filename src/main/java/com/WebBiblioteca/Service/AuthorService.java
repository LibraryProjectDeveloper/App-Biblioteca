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
import java.util.Set;
import java.util.stream.Collectors;

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
    public AuthorResponse addAuthor(AuthorRequest author) {
        Author newAuthor = new Author();
        newAuthor.setNames(author.getNames());
        newAuthor.setLastname(author.getLastname());
        newAuthor.setNationality(author.getNationality());
        newAuthor.setBirthdate(author.getBirthdate());
        newAuthor.setGender(author.getGender());
        Author savedAuthor = authorRepository.save(newAuthor);
        return new AuthorResponse(
                savedAuthor.getIdAuthor(),
                savedAuthor.getNames(),
                savedAuthor.getLastname(),
                savedAuthor.getNationality(),
                savedAuthor.getBirthdate(),
                savedAuthor.getGender()
        );
    }
    @Transactional
    public AuthorRequest updateAuthor(AuthorRequest author,Long id) {
        Author authorToUpdate = authorRepository.findByIdAuthor(id).orElseThrow(() -> new ResourceNotFoundException("Author","id",id));
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
        Author authorUpdate = authorRepository.save(authorToUpdate);
        return new AuthorRequest(
                authorUpdate.getIdAuthor(),
                authorUpdate.getNames(),
                authorUpdate.getLastname(),
                authorUpdate.getNationality(),
                authorUpdate.getBirthdate(),
                authorUpdate.getGender()
        );
    }
    public void deleteAuthor(Long id){
        authorRepository.findByIdAuthor(id).orElseThrow(()->new ResourceNotFoundException("Author","id",id));
        authorRepository.deleteById(id);
    }
    public Set<Author> castAuthorResponseListToAuthor(List<AuthorResponse> authorResponses) {
        return authorResponses.stream()
                .map(resp -> {
                    Author author = new Author();
                    author.setIdAuthor(resp.getIdAuthor());
                    author.setNames(resp.getNames());
                    author.setLastname(resp.getLastname());
                    author.setNationality(resp.getNationality());
                    author.setBirthdate(resp.getBirthdate());
                    author.setGender(resp.getGender());
                    return author;
                })
                .collect(Collectors.toSet());
    }
}
