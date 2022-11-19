package pucmm.edu.eitc.practica2mocky.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pucmm.edu.eitc.practica2mocky.encapsulaciones.*;
import pucmm.edu.eitc.practica2mocky.repo.MockRepository;

import pucmm.edu.eitc.practica2mocky.repo.ProyectoRepository;
import pucmm.edu.eitc.practica2mocky.services.*;

import java.util.*;


@Controller
public class MockController {
    final MessageSource messageSource;

    final MockRepository mockRepository;

    final UsuarioServices usuarioServices;

    final ProyectoRepository proyectoRepository;

    public MockController(MessageSource messageSource, MockRepository mockRepository, UsuarioServices usuarioServices, ProyectoRepository proyectoRepository) {
        this.messageSource = messageSource;
        this.mockRepository = mockRepository;
        this.usuarioServices = usuarioServices;
        this.proyectoRepository = proyectoRepository;
    }

    long proyectoid;

    @RequestMapping("/")
    public String index(Model model){
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user;
        if (p instanceof UserDetails){
            user=((UserDetails)p).getUsername();
        }else {
            user = p.toString();
        }
        model.addAttribute("username",user);
        List<Proyecto> proyectos = proyectoRepository.findAllByUsuario(user);
        model.addAttribute("proyectos",proyectos);
        return"index";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @RequestMapping("addProyecto")
    public String addProyecto(Model model, @RequestParam String user, RedirectAttributes redirectAttributes){
     Proyecto pro = new Proyecto();
     pro.setUsuario(user);
     pro.setEndpoints(0);
     System.out.println(pro.getUsuario());
     pro = proyectoRepository.save(pro);
     redirectAttributes.addAttribute("username",user);
     return "redirect:/proyectos";
    }

    @RequestMapping("/proyectos")
    public String verProyectos(Model model){
        return "redirect:/";
    }

    @RequestMapping("/verProyecto")
    public String verEndPoints(Model model, @RequestParam String idProyecto){
        proyectoid = Long.parseLong(idProyecto);

        List<Mock> mocks = mockRepository.findAllByIdProyecto(proyectoid);
        model.addAttribute("endpoints", mocks);
        model.addAttribute("action","verEndpoint");
        return "proyecto";
    }
    @RequestMapping("/addEndpoint")
    public String addEndpoint(Model model){
        model.addAttribute("action","addEndpoint");
        return "endPoint";
    }
    @RequestMapping(path = "/addEndpoint", method = RequestMethod.POST)
    public String crearEndpoint(Model model, WebRequest request, RedirectAttributes redirectAttributes){
        boolean jsonValido = checkJSON(request.getParameter("header"));

        Mock aux = new Mock();
        aux.setRuta(request.getParameter("path"));
        aux.setMetodo(EnumMetodo.valueOf(request.getParameter("verbo")));
        aux.setHeaders(request.getParameter("header"));
        aux.setCodigo(Integer.parseInt(request.getParameter("status")));
        aux.setContype(request.getParameter("type"));
        aux.setCuerpo(request.getParameter("body"));
        aux.setDescripcion(request.getParameter("descripcion"));
        aux.setNombre(request.getParameter("nombre"));
        aux.setExpiracion(MockServices.fecha(request.getParameter("exp")));
        aux.setTiempoRespuesta(Integer.parseInt(request.getParameter("time")));
        aux.setJwt(Boolean.parseBoolean(request.getParameter("jwt")));
        aux.setIdProyecto(proyectoid);
        System.out.println(proyectoid);

        if(jsonValido){
            aux = mockRepository.save(aux);
            Proyecto au1 = proyectoRepository.getById(proyectoid);
            au1.setEndpoints(au1.getEndpoints() + 1);
            proyectoRepository.save(au1);
            redirectAttributes.addAttribute("idProyecto", proyectoid);
            return "redirect:/verProyecto";
        }else{
            model.addAttribute("jsonV","no");
            model.addAttribute("endpoint",aux);
            return "endPoint";
        }

    }

    @RequestMapping("/verEndpoint")
    public String verEndpoint(Model model,RedirectAttributes redirectAttributes, @RequestParam String id){
        Mock mock = mockRepository.findById(Long.parseLong(id));
        if(mock != null){
            if(mock.getJwt()){
                redirectAttributes.addAttribute("id",mock.getId());
                return "redirect:/ProyectoJWT";
            }
            model.addAttribute("endpoint",mock);
            return "endPoint";
        }else{
            return "error";
        }
    }

    @RequestMapping("/ProyectoJWT")
    public String jwtEndpoint(Model model, @RequestParam long id){
        Mock mock = mockRepository.findById(id);
        model.addAttribute("endpoing",mock);
        return "endPoint";
    }

    @RequestMapping(path = "/verEndpoint", method = RequestMethod.POST)
    public String editarEndpoint(WebRequest request, RedirectAttributes redirectAttributes){
        long id = Long.parseLong(request.getParameter("id"));
        Mock aux = mockRepository.findById(id);
        if(aux != null) {
            aux.setRuta(request.getParameter("path"));
            aux.setMetodo(EnumMetodo.valueOf(request.getParameter("verbo")));
            aux.setHeaders(request.getParameter("header"));
            aux.setCodigo(Integer.parseInt(request.getParameter("status")));
            aux.setContype(request.getParameter("type"));
            aux.setCuerpo(request.getParameter("body"));
            aux.setDescripcion(request.getParameter("descripcion"));
            aux.setNombre(request.getParameter("nombre"));
            aux.setExpiracion(MockServices.fecha(request.getParameter("exp")));
            aux.setTiempoRespuesta(Integer.parseInt(request.getParameter("time")));
            aux.setJwt(Boolean.parseBoolean(request.getParameter("jwt")));
            mockRepository.save(aux);

        }
        redirectAttributes.addAttribute("idProyecto", proyectoid);
        return "redirect:/verProyecto";
    }

    @RequestMapping("/eliminarEndpoint")
    public String borrarEndpoint(RedirectAttributes redirectAttributes,@RequestParam String id){
        mockRepository.deleteById(Long.parseLong(id));
        redirectAttributes.addAttribute("idProyecto", proyectoid);
        return "redirect:/verProyecto";
    }

    @RequestMapping("/usuarios")
    public String verUsuarios(Model model){
        List<Usuario> usuarios = usuarioServices.listar();
        model.addAttribute("users", usuarios);

        return "users";
    }

    @RequestMapping("/verProyectos")
    public String verProyectosByUser(Model model,@RequestParam String user){
        List<Proyecto> misProyectos = proyectoRepository.findAllByUsuario(user);
        model.addAttribute("proyectos",misProyectos);
        return "index";
    }

    @RequestMapping("/addUser")
    public String crearUsuario(Model model){
        model.addAttribute("action","addUser");
        return "addUser";
    }

    @RequestMapping(path = "/addUser", method = RequestMethod.POST)
    public String addUsuario(WebRequest request){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Usuario aux = new Usuario();

        String permiso = request.getParameter("permisos");

        aux.setUsuario(request.getParameter("nombre"));
        aux.setNombre(request.getParameter("nombre"));
        aux.setContrasena(bCryptPasswordEncoder.encode(request.getParameter("pass")));
        aux.setActivo(true);
        Rol rol = new Rol(permiso);
        aux.setRoles(new HashSet<>(Arrays.asList(rol)));

        usuarioServices.crearUsuario(aux);

        return "redirect:/usuarios";
    }

    @RequestMapping("/modUser")
    public String modUser(Model model,@RequestParam String id){
        Usuario user = usuarioServices.buscarbyID(id);
        model.addAttribute("user",user);
        model.addAttribute("action","addUser");
        return "addUser";
    }


    @RequestMapping("/eliminarUser")
    public String deleteUser(@RequestParam String id){
        usuarioServices.borrarUsuario(id);
        return "redirect:/usuarios";
    }

    @RequestMapping("/accederEndpoint")
    public ResponseEntity<String> accederEndpoint(Model model, @RequestParam String id){
        Mock aux = mockRepository.findById(Long.parseLong(id));
        Date auxDate = new Date();
        if(auxDate.before(aux.getExpiracion())){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(aux.getContype()));
            httpHeaders = convertStringtoHeaders(httpHeaders, aux.getHeaders());

            return new ResponseEntity<>(aux.getCuerpo(),httpHeaders,aux.getCodigo());
        }
        return new ResponseEntity<>("",null,404);
    }

    private HttpHeaders convertStringtoHeaders(HttpHeaders httpHeaders, String aux) {
        JsonObject jsonObject = new JsonParser().parse(aux).getAsJsonObject();
        Iterator<String> keys = jsonObject.keySet().iterator();

        while(keys.hasNext()) {
            String key = keys.next();
            httpHeaders.set(key, String.valueOf(jsonObject.get(key)));
        }
        return httpHeaders;

    }

    private boolean checkJSON(String header) {
        try {
            Gson gson = new Gson();
            gson.fromJson(header, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

}
