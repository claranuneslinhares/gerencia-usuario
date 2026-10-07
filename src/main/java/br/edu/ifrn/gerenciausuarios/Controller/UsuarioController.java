package br.edu.ifrn.gerenciausuarios.Controller;

import br.edu.ifrn.gerenciausuarios.Model.Usuario;
import br.edu.ifrn.gerenciausuarios.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/salvar")
    public ResponseEntity<Usuario> salvar(
            @RequestBody Usuario usuario) {

        Usuario novoUsuario =
                usuarioService.salvar(usuario);

        return ResponseEntity.ok(novoUsuario);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listar() {

        return ResponseEntity.ok(
                usuarioService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(
            @PathVariable Long id) {

        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        Usuario atualizado =
                usuarioService.atualizar(id, usuario);

        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        boolean deletado =
                usuarioService.deletar(id);

        if (!deletado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}