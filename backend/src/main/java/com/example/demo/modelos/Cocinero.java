package com.example.demo.modelos;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cocineros")
public class Cocinero {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nombre;
    private String especialidad;
    
    @ManyToMany
    @JoinTable(
        name = "cocineros_platos",
        joinColumns = @JoinColumn(name = "cocinero_id"),
        inverseJoinColumns = @JoinColumn(name = "plato_id")
    )
    private Set<Plato> platos;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public Set<Plato> getPlatos() { return platos; }
    public void setPlatos(Set<Plato> platos) { this.platos = platos; }
}
