package pucmm.edu.eitc.practica2mocky.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pucmm.edu.eitc.practica2mocky.encapsulaciones.Rol;
import pucmm.edu.eitc.practica2mocky.encapsulaciones.Usuario;
import pucmm.edu.eitc.practica2mocky.repo.RolRepository;
import pucmm.edu.eitc.practica2mocky.repo.UsuarioRepository;

import java.util.*;

@Service
public class SeguridadServices implements UserDetailsService {

    final UsuarioRepository usuarioRepository;

    final RolRepository rolRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);

    public SeguridadServices(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;

        this.rolRepository = rolRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario us = usuarioRepository.findByUsuario(username);
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        assert us != null;

        for(Rol rol: us.getRoles()){
            roles.add(new SimpleGrantedAuthority(rol.getRol()));
        }
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>(roles);

        return new org.springframework.security.core.userdetails.User(us.getUsuario(), us.getContrasena(), us.isActivo(), true, true, true, grantedAuthorityList);
    }
    public List<Usuario> getUsuarios(){
        return   usuarioRepository.findAll();
    }

    public void crearUsuarioAdmin() {
        Rol RolAdmin = new Rol("ROLE_ADMIN");
        rolRepository.save(RolAdmin);
        rolRepository.save(new Rol("ROLE_USER"));

        Usuario admin = new Usuario();
        admin.setUsuario("admin");
        admin.setContrasena(bCryptPasswordEncoder.encode("admin"));
        admin.setNombre("Administrador");
        admin.setActivo(true);
        admin.setRoles(new HashSet<>(Arrays.asList(RolAdmin)));
        usuarioRepository.save(admin);
    }
}
