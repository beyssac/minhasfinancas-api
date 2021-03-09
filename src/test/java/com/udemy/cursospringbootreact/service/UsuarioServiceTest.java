package com.udemy.cursospringbootreact.service;


import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.InstanceOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.udemy.cursospringbootreact.exception.ErroAutenticacao;
import com.udemy.cursospringbootreact.exception.RegraNegocioException;
import com.udemy.cursospringbootreact.model.entity.Usuario;
import com.udemy.cursospringbootreact.model.repository.UsuarioRepository;
import com.udemy.cursospringbootreact.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Test
	public void deveValidarEmail() {
		
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao
		service.validarEmail("usuario@gmail.com");
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
	
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		Assertions.assertThrows(RegraNegocioException.class, ()->{
			//acao
			service.validarEmail("usuario@gmail.com");
		});
		
	}
	
	@Test
	public void deveSalvarUsuarioComSucesso() {
		
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
							.id(1l)
							.nome("nome")
							.email("email@email.com")
							.senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificacao
		Assertions.assertNotNull(usuarioSalvo);
		Assertions.assertEquals(usuarioSalvo.getId(), 1l);
		Assertions.assertEquals(usuarioSalvo.getNome(), "nome");
		Assertions.assertEquals(usuarioSalvo.getEmail(), "email@email.com");
		Assertions.assertEquals(usuarioSalvo.getSenha(), "senha");
		
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailCadastrado() {
		//cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//Solução Udemy com problemas
//		//acao
//		service.salvarUsuario(usuario);
//		
//		//validacao
//		Mockito.verify(repository, Mockito.never()).save(usuario);
		

		Assertions.assertThrows(RegraNegocioException.class, () ->{
			//acao e verificacao
			service.salvarUsuario(usuario);
		});
		
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificacao
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioComEmailInformado() {
		//cenario
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//verificacao
		Exception exception = Assertions.assertThrows(ErroAutenticacao.class, () ->{
			//acao
			service.autenticar("email@email.com", "senha");
		});
		
		Assertions.assertTrue(exception.getMessage().equals("Usuário não encontrado para o e-mail informado"));
		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaInformadaForInvalida() {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//verificacao
		Exception exception = Assertions.assertThrows(ErroAutenticacao.class, () ->{
			//acao
			service.autenticar(email, "senhaErrada");
		});
		
		Assertions.assertTrue(exception.getMessage().equals("Senha inválida"));		
		
	}
	
}
