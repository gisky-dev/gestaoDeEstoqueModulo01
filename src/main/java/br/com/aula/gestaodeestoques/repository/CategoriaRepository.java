package br.com.aula.gestaodeestoques.repository;
import br.com.aula.gestaodeestoques.model.Categoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoriaRepository extends Cr.CrudRepository<Categoria, Integer> {}
