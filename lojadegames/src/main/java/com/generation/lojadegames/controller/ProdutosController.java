package com.generation.lojadegames.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojadegames.model.Produtos;
import com.generation.lojadegames.repository.CategoriaRepository;
import com.generation.lojadegames.repository.ProdutosRepository;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*") //anotação essencial para o front-end consumir a API
public class ProdutosController 
{
	@Autowired
	private ProdutosRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	/* método que retornará todos os Objetos da Classe Produto
	 que estão no Banco de dados.(SELECT * FROM TABELA; DO MYSQL) */
	@GetMapping // mapeia todas as requisições e indica que o getAll responderá a todas do tipo HTTP GET do endpoint
	public ResponseEntity<List<Produtos>> getAll() //O ResponseEntity responde a requisição HTTP com uma resposta HTTP
	{
		return ResponseEntity.ok(produtoRepository.findAll()); //executa o findAll e retorna todos os objetos da classe Produto
		// e deve sempre retornar o 200 - OK 
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produtos> getById(@PathVariable Long id)
	{
		return produtoRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produtos>> getByNome(@PathVariable String nome)
	{
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@PostMapping
	public ResponseEntity<Produtos> post(@Valid @RequestBody Produtos produto)
	{
		if(categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(produtoRepository.save(produto));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@PutMapping
	public ResponseEntity<Produtos> put(@Valid @RequestBody Produtos produto)
	{
		if(produtoRepository.existsById(produto.getId()))
		{
			if(categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK)
						.body(produtoRepository.save(produto));
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id)
	{
		Optional<Produtos> produto = produtoRepository.findById(id);
		
		if(produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		produtoRepository.deleteById(id);
	}
	

}