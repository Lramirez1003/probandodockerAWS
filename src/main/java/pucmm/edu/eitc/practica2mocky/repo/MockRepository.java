package pucmm.edu.eitc.practica2mocky.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pucmm.edu.eitc.practica2mocky.encapsulaciones.Mock;

import java.util.List;

public interface MockRepository extends JpaRepository<Mock, Long> {

    List<Mock> findAllByIdProyecto(long id);
    Mock findById(long id);
}