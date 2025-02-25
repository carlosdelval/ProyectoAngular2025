package com.example.demo.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.modelos.Pedido;
import com.example.demo.repositorios.PedidoRepositorio;

@CrossOrigin
@RestController
@RequestMapping("/pedidos")
public class PedidoControlador {
    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @GetMapping("/obtener")
    public List<DTO> getPedidos() {
        List<DTO> listPedidosDTO = new ArrayList<>();
        List<Pedido> pedidos = pedidoRepositorio.findAll();
        for (Pedido p : pedidos) {
            DTO dtoPedido = new DTO();
            dtoPedido.put("id", p.getId());
            dtoPedido.put("cliente_id", p.getClienteId());
            dtoPedido.put("plato_id", p.getPlatoId());
            dtoPedido.put("total", p.getTotal());
            dtoPedido.put("fecha_pedido", p.getFecha_pedido());
            listPedidosDTO.add(dtoPedido);
        }
        return listPedidosDTO;
    }

    @PostMapping("/nuevo")
    public Pedido agregarPedido(@RequestBody DTO dtoPedido) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(((Number) dtoPedido.get("cliente_id")).intValue());
        pedido.setPlatoId(((Number) dtoPedido.get("plato_id")).intValue());
        pedido.setTotal((Double) dtoPedido.get("total"));
        pedido.setFecha_pedido(new Date()); // Se asigna la fecha actual
        return pedidoRepositorio.save(pedido);
    }

    @DeleteMapping("/eliminar")
    public void eliminarPedido(@RequestBody DTO dtoPedido) {
        int id = ((Number) dtoPedido.get("id")).intValue();
        pedidoRepositorio.deleteById(id);
    }
}
