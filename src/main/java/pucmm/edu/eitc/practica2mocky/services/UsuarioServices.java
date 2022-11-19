package pucmm.edu.eitc.practica2mocky.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pucmm.edu.eitc.practica2mocky.encapsulaciones.Usuario;
import pucmm.edu.eitc.practica2mocky.repo.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioServices {

    final UsuarioRepository usuarioRepository;

    public UsuarioServices(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void borrarUsuario(String id){
        usuarioRepository.deleteById(id);
    }

    public Usuario editarUsuario(Usuario usuario){
        Usuario us = usuarioRepository.findById(usuario.getUsuario()).orElse(null);
        if (us == null){
            return us;
        }else{
            us.setNombre(usuario.getNombre());
            us.setContrasena(usuario.getContrasena());
            us.setRoles(usuario.getRoles());
            us.setActivo(usuario.isActivo());
            return usuarioRepository.save(us);
        }
    }

    public List<Usuario> listar(){
        return usuarioRepository.findAll();
    }
    public Usuario buscarbyID(String id){
        return usuarioRepository.findById(id).orElse(null);
    }
    public Usuario buscarbyUsuario(String user){
        return  usuarioRepository.findByUsuario(user);
    }
}
