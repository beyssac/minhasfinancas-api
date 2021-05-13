package com.udemy.cursospringbootreact.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.cursospringbootreact.api.dto.AtualizaStatusDTO;
import com.udemy.cursospringbootreact.api.dto.LancamentoDTO;
import com.udemy.cursospringbootreact.exception.RegraNegocioException;
import com.udemy.cursospringbootreact.model.entity.Lancamento;
import com.udemy.cursospringbootreact.model.entity.Usuario;
import com.udemy.cursospringbootreact.model.enums.StatusLancamento;
import com.udemy.cursospringbootreact.model.enums.TipoLancamento;
import com.udemy.cursospringbootreact.service.LancamentoService;
import com.udemy.cursospringbootreact.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

	private final LancamentoService lancamentoService;
	
	private final UsuarioService usuarioService;

	
	@GetMapping()
	public ResponseEntity buscar(@RequestParam(value="descricao", required=false) String descricao,
								@RequestParam(value="mes", required=false) Integer mes,
								@RequestParam(value="ano", required=false) Integer ano,
								@RequestParam(value="tipo", required=false) TipoLancamento tipo,
								@RequestParam(value="usuario") Long idUsuario){
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setTipo(tipo);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado");
		}
		else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		
		return ResponseEntity.ok(lancamentos);
			
	}
	
	@GetMapping("{id}")
	public ResponseEntity obterLancamento(@PathVariable("id") Long id) {
		
		return lancamentoService.obterPorId(id).map(lancamento ->{
			
			return new ResponseEntity(converter(lancamento), HttpStatus.OK);
			
		}).orElseGet( ()->
			new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.NOT_FOUND)) ;
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento lancamento = converter(dto);
			lancamento = lancamentoService.salvar(lancamento);
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO dto) {
		
		return lancamentoService.obterPorId(id).map(entity ->{
			
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet( ()->
			new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST)) ;
	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizaStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
		
		return lancamentoService.obterPorId(id).map(entity -> {
			
			StatusLancamento statusLancamento = StatusLancamento.valueOf(dto.getStatus());
			
			if(statusLancamento == null)
				return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido.");
			else {
				try {
					entity.setStatus(statusLancamento);
					lancamentoService.atualizar(entity);
					return ResponseEntity.ok(entity);
				} catch (RegraNegocioException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
			}
		}).orElseGet( ()->
			new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST)) ;
			
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		
		return lancamentoService.obterPorId(id).map(entity -> {
			lancamentoService.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			
		}).orElseGet( ()-> new ResponseEntity("Lançamento não encontrado na base de dados.",HttpStatus.BAD_REQUEST));
		
	}
	
	private LancamentoDTO converter(Lancamento lancamento) {
		
		return LancamentoDTO.builder()
				.id(lancamento.getId())
				.descricao(lancamento.getDescricao())
				.valor(lancamento.getValor())
				.mes(lancamento.getMes())
				.ano(lancamento.getAno())
				.status(lancamento.getStatus().name())
				.tipo(lancamento.getTipo().name())
				.idUsuario(lancamento.getUsuario().getId())
				.build();				
				
	}
	
	private Lancamento converter(LancamentoDTO lancamentoDTO) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDescricao(lancamentoDTO.getDescricao());
		lancamento.setAno(lancamentoDTO.getAno());
		lancamento.setMes(lancamentoDTO.getMes());
		lancamento.setValor(lancamentoDTO.getValor());
		
		Usuario usuario = usuarioService.obterPorId(lancamentoDTO.getIdUsuario())
										.orElseThrow(()-> new RegraNegocioException("Usuário não encontrado para o ID informado."));
		
		lancamento.setUsuario(usuario);
		
		if(lancamentoDTO.getTipo() != null)
			lancamento.setTipo(TipoLancamento.valueOf(lancamentoDTO.getTipo()));
		
		if(lancamentoDTO.getStatus() != null)
			lancamento.setStatus(StatusLancamento.valueOf(lancamentoDTO.getStatus()));
		
		return lancamento;
	}
}
