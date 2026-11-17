package br.edu.ifrn.gerenciausuarios.dto;

import br.edu.ifrn.gerenciausuarios.Model.Usuario;

public record UsuarioResponse(Long id, String nome, String papel) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getPapel()
        );
    }
}