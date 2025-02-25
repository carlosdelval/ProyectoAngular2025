package com.example.demo.seguridad;

import java.security.Key;

import jakarta.servlet.http.HttpServletRequest;
import com.example.demo.modelos.Cliente;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class AutenticadorJWT {

    // Clave de encriptación que se usará para generar el JWT. Será diferente en
    // cada ejecución.
    private static Key key = null;

    /**
     * Desde la clase Cliente, genera un JWT que contiene su id.
     *
     * @param c El cliente que se usará para generar el JWT.
     * @return El JWT generado.
     */
    public static String codificaJWT(Cliente c) {
        @SuppressWarnings("deprecation")
        String jws = Jwts.builder()
                .setSubject(String.valueOf(c.getId())) // El ID sigue como el subject.
                .claim("id", c.getId()) // Agregamos los datos adicionales.
                .claim("nombre", c.getNombre())
                .claim("email", c.getEmail())
                .claim("username", c.getUsername())
                .claim("role", c.getRol())
                .signWith(SignatureAlgorithm.HS512, getGeneratedKey()) // Firma con HS512.
                .compact();
        return jws;
    }

    public static Cliente decodificaJWT(String jwt) {
        try {
            @SuppressWarnings("deprecation")
            var claims = Jwts.parser()
                    .setSigningKey(getGeneratedKey()) // Usa la clave de firma correcta.
                    .parseClaimsJws(jwt)
                    .getBody();

            // Extrae los datos del usuario
            Cliente cliente = new Cliente();
            cliente.setId(Integer.parseInt(claims.get("id").toString()));
            cliente.setNombre(claims.get("nombre").toString());
            cliente.setEmail(claims.get("email").toString());
            cliente.setUsername(claims.get("username").toString());
            cliente.setRol(claims.get("role").toString());

            return cliente; // Devuelve el cliente decodificado.

        } catch (Exception ex) {
            ex.printStackTrace();
            return null; // Si hay un error, devuelve null.
        }
    }

    /**
     * Desde un JWT, se obtiene el id del cliente.
     *
     * @param jwt El JWT del cual se extraerá el ID del cliente.
     * @return El id del cliente extraído del JWT.
     */
    public static int getIdClienteDesdeJWT(String jwt) {
        try {
            @SuppressWarnings("deprecation")
            String stringIdCliente = Jwts.parser()
                    .setSigningKey(getGeneratedKey()) // Establece la clave de firma.
                    .parseClaimsJws(jwt) // Analiza el JWT.
                    .getBody()
                    .getSubject(); // Obtiene el sujeto, que es el ID del cliente.

            return Integer.parseInt(stringIdCliente); // Convierte el ID en String a entero.
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1; // Si ocurre algún error, se devuelve -1.
        }
    }

    /**
     * Obtiene el id de un cliente almacenado en un JWT que proviene de un request
     * HTTP.
     *
     * @param request El request HTTP que contiene el JWT.
     * @return El id del cliente del JWT, o -1 si no se puede obtener.
     */
    public static int getIdClienteDesdeJwtIncrustadoEnRequest(HttpServletRequest request) {
        String autHeader = request.getHeader("Authorization"); // Obtiene el header de autorización.
        if (autHeader != null && autHeader.length() > 8) { // Verifica que el header sea válido.
            String jwt = autHeader.substring(7); // Elimina "Bearer " para obtener solo el JWT.
            return getIdClienteDesdeJWT(jwt); // Devuelve el id del cliente a partir del JWT.
        } else {
            return -1; // Si no hay header válido, devuelve -1.
        }
    }

    /**
     * Genera una nueva clave cada vez que se inicia el servidor.
     * 
     * @return La clave generada.
     */
    private static Key getGeneratedKey() {
        if (key == null) {
            key = MacProvider.generateKey(); // Genera una nueva clave usando MacProvider.
        }
        return key;
    }
}
