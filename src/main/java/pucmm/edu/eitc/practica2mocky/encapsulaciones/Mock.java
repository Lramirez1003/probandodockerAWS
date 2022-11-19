package pucmm.edu.eitc.practica2mocky.encapsulaciones;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
//@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mock implements Serializable{
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    long idProyecto;
    String ruta;

    @Enumerated(EnumType.STRING)
    EnumMetodo metodo;
    String headers;
    int codigo;
    String contype;
    String cuerpo;
    String descripcion;
    String nombre;
    Date expiracion;
    int tiempoRespuesta;
    Boolean jwt;

}
