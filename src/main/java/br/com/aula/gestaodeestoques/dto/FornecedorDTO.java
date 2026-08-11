package br.com.aula.gestaodeestoques.dto;
import jakarta.validation.constraints.NotBlank;

public record FornecedorDTO(
        Integer id,
        @NotBlank(message = "Nome do fornecedor é obrigatório")
        String nome
) {}
