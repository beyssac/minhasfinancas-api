package com.udemy.cursospringbootreact.api.controller;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.cursospringbootreact.api.dto.UsuarioDTO;
import com.udemy.cursospringbootreact.exception.ErroAutenticacao;
import com.udemy.cursospringbootreact.exception.RegraNegocioException;
import com.udemy.cursospringbootreact.model.entity.Usuario;
import com.udemy.cursospringbootreact.service.LancamentoService;
import com.udemy.cursospringbootreact.service.UsuarioService;

@ExtendWith(SpringExtension.class) // (JUnit4)Antigo => @RunWith(SpringRunner.class) do pacote junit para versões abaixo do springboot 2.0  
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

	static final String API = "/api/usuarios";
	static final MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService usuarioService;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		//cenario ("BACKEND")
		String email = "usuario@email.com";
		String senha = "123";
		
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		Mockito.when(usuarioService.autenticar(email, senha)).thenReturn(usuario);
		
		
		//acao ("FRONTEND")
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API.concat("/autenticar"))
												.accept(MEDIA_TYPE)
												.contentType(MEDIA_TYPE)
												.content(json);
		
		//verificacao ("FRONTEND")
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
		
	}
	
	@Test
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
		//cenario ("BACKEND")
		String email = "usuario@email.com";
		String senha = "123";
		
		Mockito.when(usuarioService.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
				
		//acao ("FRONTEND")
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
												.post(API.concat("/autenticar"))
												.accept(MEDIA_TYPE)
												.contentType(MEDIA_TYPE)
												.content(json);
		
		//verificacao ("FRONTEND")
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
	}
	
	@Test
	public void deveSalvarUmUsuario() throws Exception {
		
		String nome = "nome";
		String email = "email";
		String senha = "senha";
		
		//cenario ("BACKEND")
		Usuario usuarioSalvo = Usuario.builder().id(1l).nome(nome).email(email).senha(senha).build();
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuarioSalvo);
		
		//acao ("FRONTEND")
		UsuarioDTO dto = UsuarioDTO.builder().nome(nome).email(email).senha(senha).build();
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API)
				.accept(MEDIA_TYPE)
				.contentType(MEDIA_TYPE)
				.content(json);
		
		//verificacao ("FRONTEND")
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuarioSalvo.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuarioSalvo.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuarioSalvo.getEmail()));
		
		
	}
	
	@Test
	public void deveFalharAoSalvarUmUsuario() throws Exception {
		String nome = "nome";
		String email = "email";
		String senha = "senha";
		
		//cenario ("BACKEND")
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		
		//acao ("FRONTEND")
		UsuarioDTO dto = UsuarioDTO.builder().nome(nome).email(email).senha(senha).build();
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API)
				.accept(MEDIA_TYPE)
				.contentType(MEDIA_TYPE)
				.content(json);
		
		//verificacao ("FRONTEND")
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
