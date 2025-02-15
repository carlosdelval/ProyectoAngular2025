package com.example.demo.seguridad;

import java.security.Key;

import jakarta.servlet.http.HttpServletRequest;
import com.example.demo.modelos.Cliente;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class AutenticadorJWT {

    // Clave de encriptación que se usará para generar el JWT. Será diferente en cada ejecución.
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
            .setSubject(String.valueOf(c.getId())) // Usar String.valueOf() para asegurar que el ID se convierte a String correctamente.
            .signWith(SignatureAlgorithm.HS512, getGeneratedKey()) // Firma con el algoritmo HS512 y la clave generada.
            .compact(); // Genera el JWT compactado.
        return jws;
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
     * Obtiene el id de un cliente almacenado en un JWT que proviene de un request HTTP.
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
