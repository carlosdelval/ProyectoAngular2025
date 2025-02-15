package com.example.demo.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.seguridad.AutenticadorJWT;
import com.example.demo.modelos.Cliente;
import com.example.demo.repositorios.ClienteRepositorio;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    ClienteRepositorio clienteRepo;

    @GetMapping("/test")
    public String testCors() {
        return "CORS habilitado para este controlador.";
    }

    @GetMapping("/obtener")
    public List<DTO> getClientes() {
        List<DTO> listClientesDTO = new ArrayList<>();
        List<Cliente> clientes = clienteRepo.findAll();
        for (Cliente c : clientes) {
            DTO dtoCliente = new DTO();
            dtoCliente.put("id", c.getId());
            dtoCliente.put("nombre", c.getNombre());
            dtoCliente.put("email", c.getEmail());
            dtoCliente.put("telefono", c.getTelefono());
            dtoCliente.put("username", c.getUsername());
            listClientesDTO.add(dtoCliente);
        }
        return listClientesDTO;
    }

    @PostMapping(path = "/obtener1", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO getCliente(@RequestBody DTO soloId) {
        DTO dtoCliente = new DTO();
        Cliente c = clienteRepo.findById(Long.parseLong(soloId.get("id").toString())).orElse(null);
        if (c != null) {
            dtoCliente.put("id", c.getId());
            dtoCliente.put("nombre", c.getNombre());
            dtoCliente.put("email", c.getEmail());
            dtoCliente.put("telefono", c.getTelefono());
            dtoCliente.put("username", c.getUsername());
        } else {
            dtoCliente.put("result", "fail");
        }
        return dtoCliente;
    }

    @DeleteMapping(path = "/borrar1", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO deleteCliente(@RequestBody DTO soloId) {
        DTO dtoCliente = new DTO();
        Cliente c = clienteRepo.findById(Long.parseLong(soloId.get("id").toString())).orElse(null);
        if (c != null) {
            clienteRepo.delete(c);
            dtoCliente.put("borrado", "ok");
        } else {
            dtoCliente.put("borrado", "fail");
        }
        return dtoCliente;
    }

    @PostMapping("/addnew")
    public void addNewCliente(@RequestBody DatosAltaCliente datos) {
        clienteRepo.save(new Cliente(
            datos.id,
            datos.nombre,
            datos.email,
            datos.telefono, // Campo agregado
            datos.username,
            datos.password, // Se guarda la contraseña tal cual como se recibe
            new Date()
        ));
    }

    @PostMapping(path = "/autentica", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO autenticaCliente(@RequestBody DatosAutenticaCliente datos, HttpServletResponse response) {
        DTO dto = new DTO();
        dto.put("result", "FAIL");

        // Se realiza la búsqueda del cliente con username y password tal cual como se reciben
        Cliente clienteAuth = clienteRepo.findByUsernameAndPassword(datos.username, datos.password);
        
        if (clienteAuth != null) {
            dto.put("result", "OK");
            dto.put("jwt", AutenticadorJWT.codificaJWT(clienteAuth)); // Genera el JWT
            
            // Crear y añadir la cookie con el JWT
            Cookie cookie = new Cookie("jwt", AutenticadorJWT.codificaJWT(clienteAuth));
            cookie.setMaxAge(-1); // La cookie tendrá un ciclo de vida igual al de la sesión.
            response.addCookie(cookie);
        }
        return dto;
    }

    @GetMapping(path = "/quieneres")
    public DTO getAutenticado(HttpServletRequest request) {
        DTO dtoCliente = new DTO();
        Cookie[] cookies = request.getCookies();
        long idUsuarioAutenticado = -1;
        
        // Se busca el JWT en las cookies
        for (Cookie co : cookies) {
            if (co.getName().equals("jwt")) {
                idUsuarioAutenticado = AutenticadorJWT.getIdClienteDesdeJWT(co.getValue()); // Extrae el id del JWT
            }
        }
        
        // Recupera la información del cliente autenticado
        Cliente c = clienteRepo.findById(idUsuarioAutenticado).orElse(null);
        if (c != null) {
            dtoCliente.put("id", c.getId());
            dtoCliente.put("nombre", c.getNombre());
            dtoCliente.put("email", c.getEmail());
            dtoCliente.put("telefono", c.getTelefono()); // Campo agregado
            dtoCliente.put("username", c.getUsername());
        }
        return dtoCliente;
    }

    static class DatosAutenticaCliente {
        String username;
        String password;

        public DatosAutenticaCliente(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }
    }

    static class DatosAltaCliente {
        int id;
        String nombre;
        String email;
        String telefono; // Campo agregado
        String username;
        String password;

        public DatosAltaCliente(String nombre, String email, String telefono, String username, String password) {
            super();
            this.nombre = nombre;
            this.email = email;
            this.telefono = telefono; // Campo agregado
            this.username = username;
            this.password = password;
        }
    }
}
