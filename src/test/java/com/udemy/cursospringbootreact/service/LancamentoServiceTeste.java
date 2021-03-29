package com.udemy.cursospringbootreact.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.udemy.cursospringbootreact.exception.RegraNegocioException;
import com.udemy.cursospringbootreact.model.entity.Lancamento;
import com.udemy.cursospringbootreact.model.entity.Usuario;
import com.udemy.cursospringbootreact.model.enums.StatusLancamento;
import com.udemy.cursospringbootreact.model.enums.TipoLancamento;
import com.udemy.cursospringbootreact.model.repository.LancamentoRepository;
import com.udemy.cursospringbootreact.model.repository.LancamentoRepositoryTest;
import com.udemy.cursospringbootreact.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTeste {

	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);		
		
		Mockito.when(repository.save(Mockito.any(Lancamento.class))).thenReturn(lancamentoSalvo);
		
		//acao 
		Lancamento teste = service.salvar(lancamentoASalvar);

		//verificacao
		Assertions.assertTrue(teste.getId() == lancamentoSalvo.getId());
		Assertions.assertTrue(teste.getStatus().equals(StatusLancamento.PENDENTE));		
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		//acao e validacao
		Assertions.assertThrows(RegraNegocioException.class, ()->{
			service.salvar(lancamentoASalvar);			
		});
		
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}

	@Test
	public void deveAtualizarUmLancamento() {
		//cenario
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);	
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		Mockito.when(repository.save(Mockito.any(Lancamento.class))).thenReturn(lancamentoSalvo);
		
		//acao 
		service.atualizar(lancamentoSalvo);

		//verificacao
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		
		//acao e validacao
		Assertions.assertThrows(NullPointerException.class, ()->{
			service.atualizar(lancamentoASalvar);			
		});
		
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}
	
	//MEU A MAIS
//	@Test
//	public void naoDeveAtualizarUmlancamentoQuandoHouverErroDeValidacao() {
//		//cenario
//		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
//		lancamentoASalvar.setId(1l);	
//		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
//		
//		//acao e validacao
//		Assertions.assertThrows(RegraNegocioException.class, ()->{
//			service.atualizar(lancamentoASalvar);			
//		});
//		
//		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
//	}
	
	@Test
	public void deveDeleterUmLancamento() {
		//cenario
		Lancamento lancamentoADeletar = LancamentoRepositoryTest.criarLancamento();
		lancamentoADeletar.setId(1l);
		
		//acao
		service.deletar(lancamentoADeletar);
		
		//validacao
		Mockito.verify(repository).delete(lancamentoADeletar);
		
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenario
		Lancamento lancamentoADeletar = LancamentoRepositoryTest.criarLancamento();
		
		//acao e verificacao
		Assertions.assertThrows(NullPointerException.class, ()->{
			service.deletar(lancamentoADeletar);			
		});
		
		Mockito.verify(repository, Mockito.never()).delete(lancamentoADeletar);
	}
	
	@Test 
	public void deveFiltrarLancamento() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//acao
		List<Lancamento> resultado = service.buscar(lancamento);
		
		//verificacao
		Assertions.assertFalse(resultado.isEmpty());
		Assertions.assertTrue(resultado.size() == 1);
		Assertions.assertTrue(resultado.contains(lancamento));
				
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento(){
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		
		//acao
		service.atualizarStatus(lancamento, novoStatus);
				
		//verificacao
		Assertions.assertTrue(lancamento.getStatus().equals(StatusLancamento.EFETIVADO));
		Mockito.verify(service).atualizar(lancamento);
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		//cenario
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//acao
		Optional<Lancamento> lancamentoRetornado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertTrue(lancamentoRetornado.isPresent());
		Mockito.verify(repository).findById(id);
	}
	
	@Test
	public void deveRetornarVazioQuandoLancamentoNaoExistir() {
		//cenario
		Long id = 1l;
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//acao
		Optional<Lancamento> lancamentoRetornado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertFalse(lancamentoRetornado.isPresent());
		Mockito.verify(repository).findById(id);
	}
	
	@Test
	public void deveValidarUmLancamento() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		Usuario usuario = Usuario.builder().id(1l).build();
		lancamento.setUsuario(usuario);
		
		//acao e verificacao
		Assertions.assertDoesNotThrow(() ->{
			service.validar(lancamento);
		});
	}
	
	@Test
	public void deveLancarErroAoValidarUmLancamento() {
		//cenario
		Lancamento lancamento = new Lancamento();
		
		//acao e verificacao
		Exception excecao = new Exception();
		
		//Descricao
		//Null
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe uma descrição válida.");
		//Vazia
		lancamento.setDescricao("");
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe uma descrição válida.");
		lancamento.setDescricao("Descrição");
		
		//Mes
		//Null
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe um mês válido.");
		//Inválido		
		lancamento.setMes(13);
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe um mês válido.");
		lancamento.setMes(2);
		
		//Ano
		//Null
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe um ano válido.");
		lancamento.setAno(222);
		//Inválido
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe um ano válido.");
		lancamento.setAno(2021);
		
		//Usuario
		//Null
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe um usuário.");
		lancamento.setUsuario(new Usuario());
		//Invalido
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe um usuário.");
		lancamento.setUsuario(Usuario.builder().id(1l).build());
		
		//Valor
		//Null
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe um valor válido.");
		lancamento.setValor(BigDecimal.ZERO);
		//Inválido
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe um valor válido.");
		lancamento.setValor(BigDecimal.valueOf(200f));
		
		//Tipo
		//Null
		excecao = Assertions.assertThrows(RegraNegocioException.class, () ->{
			service.validar(lancamento);
		});
		Assertions.assertEquals(excecao.getMessage(), "Informe um tipo de lançamento.");
		lancamento.setTipo(TipoLancamento.RECEITA);
	}
	
	@Test
	public void deveObterSaldoPorUsuario() {
		//cenario
		Long id = 1l;
		BigDecimal receita = BigDecimal.valueOf(200f);
		BigDecimal despesa = BigDecimal.valueOf(100f);
		BigDecimal totalEsperado = BigDecimal.valueOf(100f);
		
		Mockito.when(repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA)).thenReturn(receita);
		Mockito.when(repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA)).thenReturn(despesa);
		
		//acao
		BigDecimal total = service.obterSaldoPorUsuario(id);
		
		//verificacao
		Assertions.assertTrue(total.compareTo(totalEsperado) == 0);
	}
	
}
