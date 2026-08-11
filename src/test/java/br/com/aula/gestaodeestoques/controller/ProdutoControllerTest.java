package br.com.aula.gestaodeestoques.controller;

import br.com.aula.gestaodeestoques.config.security.JwtAuthenticationFilter;
import br.com.aula.gestaodeestoques.dto.ProdutoDTO;
import br.com.aula.gestaodeestoques.exception.ResourceNotFoundException;
import br.com.aula.gestaodeestoques.service.ProdutoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class) // Carrega apenas o contexto web para o ProdutoController
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Objeto para simular requisições HTTP

    @MockBean // Cria um mock do serviço, pois o @WebMvcTest não carrega a camada de serviço
    private ProdutoService produtoService;

    // O filtro JWT precisa ser mockado também, pois ele faz parte da camada web
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("GET /api/produtos deve retornar lista de produtos e status 200")
    @WithMockUser // Simula um usuário autenticado (necessário porque todos os endpoints são protegidos)
    void findAll_shouldReturnListOfProducts_andStatus200() throws Exception {
        // Arrange
        ProdutoDTO produtoDTO = new ProdutoDTO(1, "Teclado", 50, BigDecimal.valueOf(150), "Periféricos", "Fornecedor Y");
        given(produtoService.findAll()).willReturn(Collections.singletonList(produtoDTO));

        // Act & Assert
        mockMvc.perform(get("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Teclado"));
    }

    @Test
    @DisplayName("GET /api/produtos/{id} deve retornar 404 quando produto não existe")
    @WithMockUser
    void findById_shouldReturn404_whenProductNotFound() throws Exception {
        // Arrange
        int productId = 99;
        given(produtoService.findById(productId)).willThrow(new ResourceNotFoundException("Produto não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/produtos/{id}", productId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/produtos/{id} deve retornar 403 Forbidden para usuário sem papel de ADMIN")
    @WithMockUser(roles = "USER") // Simula um usuário logado com o papel "USER"
    void delete_shouldReturn403Forbidden_whenUserIsNotAdmin() throws Exception {
        // Arrange
        int productId = 1;
        // Não precisamos configurar o mock do service, pois a segurança deve barrar a requisição antes de chegar lá

        // Act & Assert
        mockMvc.perform(delete("/api/produtos/{id}", productId))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/produtos/{id} deve retornar 204 No Content para usuário ADMIN")
    @WithMockUser(roles = "ADMIN") // Simula um usuário logado com o papel "ADMIN"
    void delete_shouldReturn204NoContent_whenUserIsAdmin() throws Exception {
        // Arrange
        int productId = 1;
        // doNothing() porque o método delete do service retorna void
        doNothing().when(produtoService).delete(productId);

        // Act & Assert
        mockMvc.perform(delete("/api/produtos/{id}", productId))
                .andExpect(status().isNoContent());
    }
}
