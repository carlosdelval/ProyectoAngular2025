package com.example.demo.repositorios;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.modelos.Pedido;

import jakarta.transaction.Transactional;

public interface PedidoRepositorio extends JpaRepository<Pedido, Serializable> {

    public abstract List<Pedido> findAll();
    
    public abstract Pedido findById(int id);

    public abstract List<Pedido> findByClienteId(Long clienteId);

    @SuppressWarnings("unchecked")
    @Transactional
    public abstract Pedido save(Pedido pedido);
    public abstract Pedido deleteById(int id);
}
