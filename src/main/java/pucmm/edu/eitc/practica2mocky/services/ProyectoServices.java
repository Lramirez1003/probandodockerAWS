package pucmm.edu.eitc.practica2mocky.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pucmm.edu.eitc.practica2mocky.encapsulaciones.Proyecto;
import pucmm.edu.eitc.practica2mocky.repo.ProyectoRepository;

import java.util.List;

@Service
public class ProyectoServices {

    @Autowired
    private static ProyectoRepository proyectoRepository;

    @Transactional
    public Proyecto crearProyecto(Proyecto proyecto){
        return proyectoRepository.save(proyecto);
    }

    public static List<Proyecto> buscarProyectosPorUsername(String username) {
        return proyectoRepository.findAllByUsuario(username);

    }
}
