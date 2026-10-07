package br.edu.ifrn.gerenciausuarios.Service;

import br.edu.ifrn.gerenciausuarios.Model.Usuario;
import br.edu.ifrn.gerenciausuarios.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario atualizar(Long id, Usuario usuario) {

        Optional<Usuario> usuarioExistente =
                usuarioRepository.findById(id);

        if (usuarioExistente.isEmpty()) {
            return null;
        }

        Usuario u = usuarioExistente.get();

        u.setNome(usuario.getNome());
        u.setPapel(usuario.getPapel());

        return usuarioRepository.save(u);
    }

    public boolean deletar(Long id) {

        if (!usuarioRepository.existsById(id)) {
            return false;
        }

        usuarioRepository.deleteById(id);

        return true;
    }
}