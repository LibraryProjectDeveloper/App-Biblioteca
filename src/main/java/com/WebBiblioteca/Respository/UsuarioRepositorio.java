package com.WebBiblioteca.Respository;

import com.WebBiblioteca.Model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UsuarioRepositorio {
    private final Map<Long, Usuario> listUsuarios = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1L);
}
