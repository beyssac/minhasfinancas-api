package com.udemy.cursospringbootreact.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import com.udemy.cursospringbootreact.model.entity.Lancamento;
import com.udemy.cursospringbootreact.model.enums.StatusLancamento;
import com.udemy.cursospringbootreact.model.enums.TipoLancamento;

@ExtendWith(SpringExtension.class) // (JUnit4)Antigo => @RunWith(SpringRunner.class) do pacote junit para versões abaixo do springboot 2.0  
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenario
		Lancamento lancamento = criarLancamento();		
		//acao
		Lancamento lancamentoSalvo = repository.save(lancamento);		
		
		//verificacao
		Assertions.assertNotNull(lancamentoSalvo.getId());
		
	}

	@Test
	public void deveDeletarUmLancamento() {
		
		Lancamento lancamento = criarEPersistirUmLancamento();		
		Lancamento lancamentoSalvo = entityManager.find(Lancamento.class, lancamento.getId());
		
		//acao
		repository.delete(lancamentoSalvo);
		
		//verificacao
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		Assertions.assertNull(lancamentoInexistente);
		
	}

	private Lancamento criarEPersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();		
		entityManager.persist(lancamento);
		return lancamento;
	}
	
	@Test
	public void deveAtualizarLancamento() {
		//cenario
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		Integer ano = 2021;
		StatusLancamento status = StatusLancamento.EFETIVADO;
		lancamento.setAno(ano);
		lancamento.setStatus(status);
		
		//acao
		repository.save(lancamento);		
		
		//verificacao
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		Assertions.assertTrue(lancamentoAtualizado.getAno().equals(ano));
		Assertions.assertTrue(lancamentoAtualizado.getStatus().equals(status));
		
		
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		//cenario
		Lancamento lancamento = criarEPersistirUmLancamento();
		Long id = lancamento.getId();
		
		//acao
		Optional<Lancamento> lancamentoBuscado = repository.findById(id);
		
		//verificacao
		Assertions.assertTrue(lancamentoBuscado.isPresent());
	}
	
	public static Lancamento criarLancamento() {
		Lancamento lancamento = Lancamento.builder()
													.ano(2019)
													.mes(1)
													.descricao("Esse é uma lançamento")
													.valor(BigDecimal.valueOf(10))
													.tipo(TipoLancamento.RECEITA)
													.status(StatusLancamento.PENDENTE)
													.dataCadastro(LocalDate.now()).build();
		return lancamento;
	}
}
