package br.edu.ifrn.gerenciausuarios.Controller;

import br.edu.ifrn.gerenciausuarios.Model.Usuario;
import br.edu.ifrn.gerenciausuarios.Service.AuthService;
import br.edu.ifrn.gerenciausuarios.security.JwtService;
import br.edu.ifrn.gerenciausuarios.dto.UsuarioResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@RequestBody CadastroRequest pedido) {
        return UsuarioResponse.de(
                authService.cadastrar(pedido.nome(), pedido.email(), pedido.senha())
        );
    }

    @PostMapping("/login")
    public LoginResponse entrar(@RequestBody LoginRequest pedido) {
        Usuario usuario = authService.autenticar(pedido.email(), pedido.senha());

        return new LoginResponse(
                jwtService.gerarToken(usuario),
                "Bearer",
                UsuarioResponse.de(usuario)
        );
    }

    public record CadastroRequest(String nome, String email, String senha) {
    }

    public record LoginRequest(String email, String senha) {
    }

    public record LoginResponse(String accessToken, String tokenType, UsuarioResponse usuario) {
    }
}