package com.example.demo.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
            dtoCliente.put("rol", c.getRol());
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
            dtoCliente.put("rol", c.getRol());
        } else {
            dtoCliente.put("result", "fail");
        }
        return dtoCliente;
    }

    @PutMapping(path = "/editar1", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO editarCliente(@RequestBody DTO datosActualizados) {
        DTO dtoRespuesta = new DTO();
        Cliente c = clienteRepo.findById(Long.parseLong(datosActualizados.get("id").toString())).orElse(null);

        if (c != null) {
            c.setNombre(datosActualizados.get("nombre").toString());
            c.setEmail(datosActualizados.get("email").toString());
            c.setTelefono(datosActualizados.get("telefono").toString());
            c.setUsername(datosActualizados.get("username").toString());
            c.setRol(datosActualizados.get("rol").toString());

            clienteRepo.save(c);
            dtoRespuesta.put("editado", "ok");
        } else {
            dtoRespuesta.put("editado", "fail");
        }

        return dtoRespuesta;
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
                new Date(),
                "cliente" // Rol por defecto
        ));
    }

    @PostMapping(path = "/autentica", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO autenticaCliente(@RequestBody DatosAutenticaCliente datos, HttpServletResponse response) {
        DTO dto = new DTO();
        dto.put("result", "FAIL");

        Cliente clienteAuth = clienteRepo.findByUsernameAndPassword(datos.username, datos.password);

        if (clienteAuth != null) {
            String jwt = AutenticadorJWT.codificaJWT(clienteAuth);
            dto.put("result", "OK");
            dto.put("jwt", jwt);
            dto.put("id", clienteAuth.getId());
            dto.put("nombre", clienteAuth.getNombre());
            dto.put("email", clienteAuth.getEmail());
            dto.put("username", clienteAuth.getUsername());
            dto.put("role", clienteAuth.getRol());

            // Configurar cookie con el JWT
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24 * 7); // 7 días
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
            dtoCliente.put("rol", c.getRol());
        }
        return dtoCliente;
    }

    @GetMapping("/datos-usuario")
    public ResponseEntity<?> obtenerDatosUsuario(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }

        String token = authHeader.substring(7);
        int idCliente = AutenticadorJWT.getIdClienteDesdeJWT(token);
        Cliente cliente = clienteRepo.findById(idCliente);

        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", cliente.getId());
        response.put("nombre", cliente.getNombre());
        response.put("email", cliente.getEmail());
        response.put("username", cliente.getUsername());
        response.put("role", cliente.getRol());

        return ResponseEntity.ok(response);
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
        String telefono;
        String username;
        String password;
        String rol;

        public DatosAltaCliente(String nombre, String email, String telefono, String username, String password,
                String rol) {
            super();
            this.nombre = nombre;
            this.email = email;
            this.telefono = telefono;
            this.username = username;
            this.password = password;
            this.rol = rol;
        }
    }

    @PostMapping("/verificar-token")
    public DTO verificarToken(@RequestBody Map<String, String> body) {
        DTO dto = new DTO();
        String token = body.get("token");

        if (token == null || token.isEmpty()) {
            dto.put("valid", false);
            return dto;
        }

        Cliente cliente = AutenticadorJWT.decodificaJWT(token);

        if (cliente != null) {
            dto.put("valid", true);
            dto.put("user", Map.of(
                    "id", cliente.getId(),
                    "nombre", cliente.getNombre(),
                    "email", cliente.getEmail(),
                    "username", cliente.getUsername(),
                    "role", cliente.getRol()));
        } else {
            dto.put("valid", false);
        }

        return dto;
    }

    @PostMapping("/verificar-usuario")
    public DTO verificarUsuario(@RequestBody DTO datos) {
        DTO respuesta = new DTO();
        String username = datos.get("username").toString();

        Cliente clienteExistente = clienteRepo.findByUsername(username);
        if (clienteExistente != null) {
            respuesta.put("existe", true);
        } else {
            respuesta.put("existe", false);
        }
        return respuesta;
    }

    @PostMapping("/verificar-admin")
    public DTO verificarAdmin(@RequestBody DTO datos) {
        DTO respuesta = new DTO();
        String username = datos.get("username").toString();

        Cliente cliente = clienteRepo.findByUsername(username);
        if (cliente != null && "admin".equals(cliente.getRol())) {
            respuesta.put("esAdmin", true);
        } else {
            respuesta.put("esAdmin", false);
        }
        return respuesta;
    }
}
