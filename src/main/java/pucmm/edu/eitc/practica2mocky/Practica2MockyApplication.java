package pucmm.edu.eitc.practica2mocky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import pucmm.edu.eitc.practica2mocky.services.SeguridadServices;

@SpringBootApplication
public class Practica2MockyApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Practica2MockyApplication.class, args);
        SeguridadServices seguridadServices = (SeguridadServices) applicationContext.getBean("seguridadServices");
        seguridadServices.crearUsuarioAdmin();
    }

}
