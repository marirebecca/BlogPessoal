package com.generation.blogPessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogPessoal.model.Usuario;
import com.generation.blogPessoal.repository.UsuarioRepository;
import com.generation.blogPessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testeRestTamplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
	}
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar Um Usuario")
	public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
				"Paulo Antunes", "htpps://i.imgur.com/FETvs20.png", "paulo_antunes@email.com.br", "12345678"));
		
		ResponseEntity<Usuario> resposta = testeRestTamplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getFoto(), resposta.getBody().getFoto());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
		
	}
	
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuario")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Maria da Silva", "https://i.imgur.com/NtyGneo.jpg", "maria_silva@email.com.br", "12345678"));
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
				"Maria da Silva", "https://i.imgur.com/NtyGneo.jpg", "maria_silva@email.com.br", "12345678"));
		
		ResponseEntity<Usuario> resposta = testeRestTamplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		
	}
	
	
	@Test
	@Order(3)
	@DisplayName("Alterar um Usuario")
	public void deveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,
				"Juliana Andrews", "htpps://i.imgur.com/FETvs20.png", "juliana_andrews@email.com.br", "juliana123"));
				
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), 
				"Juliana Andrews Ramos", "htpps://i.imgur.com/T12NIp9.jpg", "juliana_ramos@email.com.br", "juliana123");		
				
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> resposta = testeRestTamplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getFoto(), resposta.getBody().getFoto());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());	
		
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuarios")
	public void deveMostrarTodosUsuarios() {
		
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Sabrina Sanches", "htpps://i.imgur.com/FETvs20.png", "sabrina_sanches@email.com.br", "sabrina123"));

		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Ricardo Marques", "htpps://i.imgur.com/FETvs20.png", "ricardo_marques@email.com.br", "ricardo123"));
		
		ResponseEntity<String> resposta = testeRestTamplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
		
}
