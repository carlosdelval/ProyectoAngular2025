package com.example.demo.repositorios;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.modelos.Cocinero;

import jakarta.transaction.Transactional;

public interface CocineroRepositorio extends JpaRepository<Cocinero, Serializable> {

    @Bean
    public abstract List<Cocinero> findAll();
    
    public abstract Cocinero findById(int id);
    
    public abstract Cocinero findByNombre(String nombre);
    
    public abstract Cocinero findByEspecialidad(String especialidad);

    @SuppressWarnings("unchecked")
    @Transactional
    public abstract Cocinero save(Cocinero cocinero);
}
