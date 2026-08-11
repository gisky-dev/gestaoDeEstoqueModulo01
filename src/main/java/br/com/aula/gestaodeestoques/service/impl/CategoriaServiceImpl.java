package br.com.aula.gestaodeestoques.service.impl;
import br.com.aula.gestaodeestoques.dto.CategoriaDTO;
import br.com.aula.gestaodeestoques.exception.ResourceNotFoundException;
import br.com.aula.gestaodeestoques.mapper.CategoriaMapper;
import br.com.aula.gestaodeestoques.model.Categoria;
import br.com.aula.gestaodeestoques.repository.CategoriaRepository;
import br.com.aula.gestaodeestoques.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private CategoriaMapper mapper;

    @Override
    public CategoriaDTO create(CategoriaDTO categoriaDTO) {
        Categoria categoria = mapper.toEntity(categoriaDTO);
        Categoria savedCategoria = repository.save(categoria);
        return mapper.toDTO(savedCategoria);
    }

    @Override
    public List<CategoriaDTO> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaDTO findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }
    // ... Implementação dos métodos update e delete ...
}
