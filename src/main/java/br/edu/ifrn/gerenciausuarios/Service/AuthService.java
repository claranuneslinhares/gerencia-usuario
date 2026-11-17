package br.edu.ifrn.gerenciausuarios.Service;

import br.edu.ifrn.gerenciausuarios.Model.Usuario;
import br.edu.ifrn.gerenciausuarios.Repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Pattern EMAIL = Pattern.compile(
            "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
    );

    private static final Set<String> PAPEIS = Set.of(
            "GERENTE", "VENDEDOR", "CLIENTE"
    );

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario cadastrar(String nome, String email, String senha) {
        if (nome == null || nome.isBlank() || nome.strip().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um nome válido.");
        }

        String emailNormalizado = normalizarEmail(email);
        if (emailNormalizado.length() > 254 || !EMAIL.matcher(emailNormalizado).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um e-mail válido.");
        }

        if (senha == null || senha.isBlank() || senha.length() < 8 || excedeLimiteBCrypt(senha)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A senha deve ter no mínimo 8 caracteres e até 72 bytes."
            );
        }

        Usuario usuario = new Usuario(nome.strip(), "CLIENTE");
        usuario.setEmail(emailNormalizado);
        usuario.setSenhaHash(passwordEncoder.encode(senha));

        try {
            return repository.saveAndFlush(usuario);
        } catch (DataIntegrityViolationException erro) {
            if (repository.findByEmail(emailNormalizado).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
            }
            throw erro;
        }
    }

    public Usuario autenticar(String email, String senha) {
        if (senha == null || excedeLimiteBCrypt(senha)) {
            throw credenciaisInvalidas();
        }

        Usuario usuario = repository.findByEmail(normalizarEmail(email))
                .orElseThrow(this::credenciaisInvalidas);

        if (usuario.getSenhaHash() == null
                || usuario.getPapel() == null
                || !PAPEIS.contains(usuario.getPapel())
                || !passwordEncoder.matches(senha, usuario.getSenhaHash())) {
            throw credenciaisInvalidas();
        }

        return usuario;
    }

    private String normalizarEmail(String email) {
        return email == null ? "" : email.strip().toLowerCase(Locale.ROOT);
    }

    private boolean excedeLimiteBCrypt(String senha) {
        return senha.getBytes(StandardCharsets.UTF_8).length > 72;
    }

    private ResponseStatusException credenciaisInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
    }
}