package br.com.aula.gestaodeestoques.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
@Table("PRODUTO")
public record Produto(@Id Integer id, String nome, int quantidade, BigDecimal preco, Integer categoriaId, Integer fornecedorId) {}
