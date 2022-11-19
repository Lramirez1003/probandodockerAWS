package pucmm.edu.eitc.practica2mocky.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pucmm.edu.eitc.practica2mocky.encapsulaciones.Proyecto;

import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findAllByUsuario(String username);
}
