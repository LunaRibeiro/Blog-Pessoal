package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tb_postagens")
public class Postagem {
	//long é tipo o big int no mysql
	@Id								
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//Para ser obrigatorio preencher
	@NotBlank (message = "O título não pode estar vazio")
	//tamanho do titulo
	@Size(min = 5, max = 100, message = "Deve conter entre 5 a 100 caracteres")
	private String titulo;
	
	//Para ser obrigatorio preencher
	@NotBlank (message = "O texto não pode estar vazio")
	//tamanho do texto
	@Size(min = 5, max = 100, message = "Deve conter entre 10 a 1000 caracteres")
	private String texto;
	
	//atualiza a data sozinho
	@UpdateTimestamp
	private LocalDateTime data;
	
	
	/*Métodos Get and Set*/
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
}
