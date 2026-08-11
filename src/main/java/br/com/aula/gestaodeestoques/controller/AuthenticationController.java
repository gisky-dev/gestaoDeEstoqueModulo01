package br.com.aula.gestaodeestoques.controller;

import br.com.aula.gestaodeestoques.config.security.JwtTokenProvider;
import br.com.aula.gestaodeestoques.dto.auth.JwtAuthenticationResponse;
import br.com.aula.gestaodeestoques.dto.auth.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticate(@RequestBody LoginRequest request) {
        // 1. Tenta autenticar o usuário usando o mecanismo do Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password())
        );

        // 2. Se a autenticação for bem-sucedida, o objeto Authentication conterá os detalhes do usuário
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Gera o token JWT
        String token = jwtTokenProvider.generateToken(userDetails);

        // 4. Retorna o token na resposta
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
}
