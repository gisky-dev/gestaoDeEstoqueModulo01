package br.com.aula.gestaodeestoques.controller;

import br.com.aula.gestaodeestoques.dto.ProdutoDTO;
import br.com.aula.gestaodeestoques.dto.ProdutoFormDTO;
import br.com.aula.gestaodeestoques.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
@SecurityRequirement(name = "bearerAuth") // Exige autenticação JWT para todos os endpoints nesta classe
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista detalhada de todos os produtos cadastrados.")
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Busca um produto por ID", description = "Retorna os detalhes de um produto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Cria um novo produto", description = "Acessível apenas por usuários com papel 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é ADMIN)")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoDTO> create(@Valid @RequestBody ProdutoFormDTO produtoFormDTO) {
        ProdutoDTO newDto = service.create(produtoFormDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.id()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @Operation(summary = "Deleta um produto", description = "Acessível apenas por usuários com papel 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
