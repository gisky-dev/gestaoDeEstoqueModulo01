package br.com.aula.gestaodeestoques.mapper;

import br.com.aula.gestaodeestoques.dto.FornecedorDTO;
import br.com.aula.gestaodeestoques.model.Fornecedor;
import org.springframework.stereotype.Component;

@Component
public class FornecedorMapper {

    public FornecedorDTO toDTO(Fornecedor fornecedor) {
        return new FornecedorDTO(
                fornecedor.id(),
                fornecedor.nome()
        );
    }

    public Fornecedor toEntity(FornecedorDTO fornecedorDTO) {
        return new Fornecedor(
                fornecedorDTO.id(),
                fornecedorDTO.nome()
        );
    }
}