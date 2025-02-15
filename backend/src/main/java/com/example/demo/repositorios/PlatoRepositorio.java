package com.example.demo.repositorios;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.modelos.Plato;

import jakarta.transaction.Transactional;

public interface PlatoRepositorio extends JpaRepository<Plato, Serializable> {

    @Bean
    public abstract List<Plato> findAll();
    
    public abstract Plato findById(int id);

    public abstract Plato findByNombre(String nombre);

    public abstract Plato findByDescripcion(String descripcion);

    public abstract Plato findByPrecio(double precio);

    public abstract Plato findByImg(String img);

    public abstract List<Plato> findByDestacado(Boolean destacado);

    @SuppressWarnings("unchecked")
    @Transactional
    public abstract Plato save(Plato plato);
}
