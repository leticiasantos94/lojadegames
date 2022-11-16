package com.generation.lojadegames.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.generation.lojadegames.model.Produtos;

@Repository //classe abstrata que herda a Interface JpaRepository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> //Produto é a classe que gera a tabela e Long é a primary key
{	
	public List<Produtos> findAllByNomeContainingIgnoreCase(@Param("nome") String Nome);

}