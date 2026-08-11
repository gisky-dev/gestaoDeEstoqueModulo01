package br.com.aula.gestaodeestoques.service.impl;

import br.com.aula.gestaodeestoques.dto.ProdutoDTO;
import br.com.aula.gestaodeestoques.dto.ProdutoFormDTO;
import br.com.aula.gestaodeestoques.exception.ResourceNotFoundException;
import br.com.aula.gestaodeestoques.mapper.ProdutoMapper;
import br.com.aula.gestaodeestoques.model.Categoria;
import br.com.aula.gestaodeestoques.model.Fornecedor;
import br.com.aula.gestaodeestoques.model.Produto;
import br.com.aula.gestaodeestoques.repository.CategoriaRepository;
import br.com.aula.gestaodeestoques.repository.FornecedorRepository;
import br.com.aula.gestaodeestoques.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito para JUnit 5
class ProdutoServiceImplTest {

    @Mock // 1. Cria um "simulador" para o ProdutoRepository
    private ProdutoRepository produtoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private FornecedorRepository fornecedorRepository;
    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks // 2. Cria uma instância real do ProdutoServiceImpl, injetando os simuladores (mocks) acima
    private ProdutoServiceImpl produtoService;

    @Test
    @DisplayName("Deve retornar um ProdutoDTO quando o ID existe")
    void findById_shouldReturnProductDTO_whenIdExists() {
        // Arrange (Given)
        int productId = 1;
        Produto produto = new Produto(productId, "Notebook", 10, BigDecimal.TEN, 1, 1);
        Categoria categoria = new Categoria(1, "Eletrônicos");
        Fornecedor fornecedor = new Fornecedor(1, "Fornecedor X", "11.111.111/0001-11");
        ProdutoDTO expectedDto = new ProdutoDTO(productId, "Notebook", 10, BigDecimal.TEN, "Eletrônicos", "Fornecedor X");

        // "Quando o repository.findById for chamado com o ID 1, então retorne nosso produto de teste"
        when(produtoRepository.findById(productId)).thenReturn(Optional.of(produto));
        // Configura o comportamento dos outros mocks que são chamados internamente
        when(categoriaRepository.findById(anyInt())).thenReturn(Optional.of(categoria));
        when(fornecedorRepository.findById(anyInt())).thenReturn(Optional.of(fornecedor));
        when(produtoMapper.toDTO(any(), any(), any())).thenReturn(expectedDto);

        // Act (When)
        ProdutoDTO result = produtoService.findById(productId);

        // Assert (Then)
        assertNotNull(result);
        assertEquals(expectedDto.id(), result.id());
        assertEquals(expectedDto.nome(), result.nome());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o ID não existe")
    void findById_shouldThrowResourceNotFoundException_whenIdDoesNotExist() {
        // Arrange (Given)
        int productId = 99;
        // "Quando o repository.findById for chamado com o ID 99, então retorne um Optional vazio"
        when(produtoRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert (When & Then)
        // Verifica se a execução do método findById lança a exceção esperada
        assertThrows(ResourceNotFoundException.class, () -> {
            produtoService.findById(productId);
        });
    }

    @Test
    @DisplayName("Deve chamar o método delete do repositório quando o produto existe")
    void delete_shouldCallRepositoryDelete_whenProductExists() {
        // Arrange (Given)
        int productId = 1;
        when(produtoRepository.existsById(productId)).thenReturn(true);
        // doNothing() é usado para métodos que não retornam nada (void)
        doNothing().when(produtoRepository).deleteById(productId);

        // Act (When)
        produtoService.delete(productId);

        // Assert (Then)
        // Verifica se o método deleteById do repositório foi chamado exatamente uma vez com o ID correto
        verify(produtoRepository, times(1)).deleteById(productId);
    }
}
