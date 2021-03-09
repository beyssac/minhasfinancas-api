package com.udemy.cursospringbootreact.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.udemy.cursospringbootreact.exception.ErroAutenticacao;
import com.udemy.cursospringbootreact.exception.RegraNegocioException;
import com.udemy.cursospringbootreact.model.entity.Usuario;
import com.udemy.cursospringbootreact.model.repository.UsuarioRepository;
import com.udemy.cursospringbootreact.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	private UsuarioRepository repository;
		
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o e-mail informado");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida");
		}
		
		return usuario.get();
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		
		boolean existe = repository.existsByEmail(email);
		if(existe)
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse e-mail");
		
	}

}
