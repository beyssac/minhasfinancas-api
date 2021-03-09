package com.udemy.cursospringbootreact.service;

import com.udemy.cursospringbootreact.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
}
