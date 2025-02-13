package com.example.demo.repositorios;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.modelos.Cliente;

import jakarta.transaction.Transactional;

public interface ClienteRepositorio extends JpaRepository<Cliente, Serializable> {

    @Bean
    public abstract List<Cliente> findAll();
    
    public abstract Cliente findById(int id);
    
    public abstract Cliente findByUsername(String username);
    
    public abstract Cliente findByUsernameAndPassword(String username, String password);

    @SuppressWarnings("unchecked")
    @Transactional
    public abstract Cliente save(Cliente cliente);
}
