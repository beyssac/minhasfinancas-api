package com.udemy.cursospringbootreact.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udemy.cursospringbootreact.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
