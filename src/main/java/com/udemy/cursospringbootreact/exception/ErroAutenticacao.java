package com.udemy.cursospringbootreact.exception;

public class ErroAutenticacao extends RuntimeException{

	public ErroAutenticacao(String mensagem) {
		super(mensagem);
	}
	
}
