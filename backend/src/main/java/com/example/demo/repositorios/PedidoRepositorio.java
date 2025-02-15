package com.example.demo.repositorios;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.modelos.Pedido;

import jakarta.transaction.Transactional;

public interface PedidoRepositorio extends JpaRepository<Pedido, Serializable> {

    @Bean
    public abstract List<Pedido> findAll();
    
    public abstract Pedido findById(int id);

    public abstract Pedido findByClienteId(int clienteId);

    public abstract Pedido findByDescripcion(String descripcion);

    public abstract Pedido findByTotal(double total);

    public abstract Pedido findByFechaPedido(Date fechaPedido);

    @SuppressWarnings("unchecked")
    @Transactional
    public abstract Pedido save(Pedido pedido);
}
