package br.com.aula.gestaodeestoques.mapper;

import br.com.aula.gestaodeestoques.dto.ProdutoDTO;
import br.com.aula.gestaodeestoques.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public ProdutoDTO toDTO(
            Produto produto,
            String nomeCategoria,
            String nomeFornecedor
    ) {
        return new ProdutoDTO(
                produto.id(),
                produto.nome(),
                produto.quantidade(),
                produto.preco(),
                nomeCategoria,
                nomeFornecedor
        );
    }

    public Produto toEntity(ProdutoDTO produtoDTO, Integer categoriaId, Integer fornecedorId) {
        return new Produto(
                produtoDTO.id(),
                produtoDTO.nome(),
                produtoDTO.quantidade(),
                produtoDTO.preco(),
                categoriaId,
                fornecedorId
        );
    }
}