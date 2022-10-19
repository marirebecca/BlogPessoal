package com.generation.blogPessoal.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogPessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
		
		usuarioRepository.save(new Usuario(0L, "João da Silva", "http://i.imgur.com/h4t8loa.jpg", "joao@email.com.br", "123456"));
		
		usuarioRepository.save(new Usuario(0L, "Manuela da Silva", "http://i.imgur.com/h4t8loa.jpg", "manuela@email.com.br", "123456"));
		
		usuarioRepository.save(new Usuario(0L, "Adriana da Silva", "http://i.imgur.com/h4t8loa.jpg", "adriana@email.com.br", "123456"));
		
		usuarioRepository.save(new Usuario(0L, "Paulo Antunes", "http://i.imgur.com/h4t8loa.jpg", "paulo@email.com.br", "123456"));
	}
	
	@Test
	@DisplayName("Retorna 1 usuario")
	public void deveRetornarUsuario() {
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@email.com.br");
		assertTrue(usuario.get().getUsuario().equals("joao@email.com.br"));
	}
	
	@Test
	@DisplayName("Retorna 3 usuarios")
	public void deveRetornarTresUsuarios() {
		
		List <Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getUsuario().equals("João da Silva"));
		assertTrue(listaDeUsuarios.get(1).getUsuario().equals("Manuela da Silva"));
		assertTrue(listaDeUsuarios.get(2).getUsuario().equals("Adriana da Silva"));
	}
	
}

