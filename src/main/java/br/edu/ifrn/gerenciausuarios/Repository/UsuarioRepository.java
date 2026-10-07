package br.edu.ifrn.gerenciausuarios.Repository;

import br.edu.ifrn.gerenciausuarios.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}