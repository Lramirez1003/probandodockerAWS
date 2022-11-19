package pucmm.edu.eitc.practica2mocky.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pucmm.edu.eitc.practica2mocky.encapsulaciones.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findByUsuario(String usuario);
}
