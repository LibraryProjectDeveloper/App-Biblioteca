package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.Autor.AutorRequest;
import com.WebBiblioteca.DTO.Autor.AutorResponse;
import com.WebBiblioteca.Model.Autor;
import com.WebBiblioteca.Repository.AutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AutorService {
    private final AutorRepository autorRepository;

    public List<AutorResponse> getAllAuthors() {
        List<AutorResponse> autorResponseList = autorRepository.findAll().
                stream()
                .map(autor -> new AutorResponse(
                        autor.getIdAutor(),
                        autor.getNombres(),
                        autor.getApellidos(),
                        autor.getNacionalidad(),
                        autor.getFechaNacimiento(),
                        autor.getGenero()
                ))
                .toList();
        if (autorResponseList.isEmpty()) {
            throw new RuntimeException("No hay autores disponibles");
        }
        return autorResponseList;
    }

    @Transactional
    public AutorRequest updateAutor(AutorRequest autor) {
        Autor autorToUpdate = autorRepository.findByIdAutor(autor.getIdAutor()).orElse(null);
        if (autorToUpdate == null){
            throw new RuntimeException("El autor no existe");
        }

        if (autor.getNombres() != null) {
            autorToUpdate.setNombres(autor.getNombres());
        }
        if (autor.getApellidos() != null) {
            autorToUpdate.setApellidos(autor.getApellidos());
        }
        if (autor.getNacionalidad() != null) {
            autorToUpdate.setNacionalidad(autor.getNacionalidad());
        }
        if (autor.getFechaNacimiento() != null) {
            autorToUpdate.setFechaNacimiento(autor.getFechaNacimiento());
        }
        if (autor.getGenero() != null) {
            autorToUpdate.setGenero(autor.getGenero());
        }
        autorRepository.save(autorToUpdate);
        return autor;
    }
}
