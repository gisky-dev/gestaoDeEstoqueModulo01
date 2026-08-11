package br.com.aula.gestaodeestoques.service;
import br.com.aula.gestaodeestoques.dto.FornecedorDTO;
import java.util.List;
public interface FornecedorService {
    FornecedorDTO create(FornecedorDTO fornecedorDTO);
    List<FornecedorDTO> findAll();
    // ... outros métodos ...
}
