package com.udemy.cursospringbootreact.model.repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.udemy.cursospringbootreact.model.entity.Usuario;


@ExtendWith(SpringExtension.class) // (JUnit4)Antigo => @RunWith(SpringRunner.class) do pacote junit para versões abaixo do springboot 2.0  
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//Cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//Acao ou execucao
		boolean result = repository.existsByEmail("usuario@gmail.com");
		
		//Verificacao
		Assertions.assertTrue(result);
		
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComEmail() {
		//Cenario

		//Acao
		boolean result = repository.existsByEmail("usuario@gmail.com");
		
		//Verificacao
		Assertions.assertFalse(result);
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//Cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@gmail.com").build();
		//Acao
		Usuario usuarioSalvo = repository.save(usuario);
		//Verificacao
		Assertions.assertNotNull(usuarioSalvo.getId());
	}
	
	@Test
	public void deveBuscarUsuarioPorEmail() {
		//Cenario
		Usuario usuario = criarUsuario();
		Usuario usuarioSalvo = entityManager.persist(usuario);
		//Acao
		Optional<Usuario> usuarioPesquisado = repository.findByEmail("usuario@gmail.com");
		//Verificacao
		Assertions.assertTrue(usuarioPesquisado.isPresent());
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		//Cenario	
		
		//Acao
		Optional<Usuario> usuarioPesquisado = repository.findByEmail("usuario@gmail.com");
		//Verificacao
		Assertions.assertFalse(usuarioPesquisado.isPresent());
	}
	
	public static Usuario criarUsuario() {
		return Usuario.builder().nome("usuario").email("usuario@gmail.com").build();
	}
}
