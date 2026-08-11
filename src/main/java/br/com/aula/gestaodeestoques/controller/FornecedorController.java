package br.com.aula.gestaodeestoques.controller;

import br.com.aula.gestaodeestoques.dto.FornecedorDTO;
import br.com.aula.gestaodeestoques.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<FornecedorDTO> create(@Valid @RequestBody FornecedorDTO fornecedorDTO) {
        FornecedorDTO newDto = service.create(fornecedorDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.id()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    // ... Implementar outros endpoints (findById, update, delete) seguindo o padrão acima ...
}
