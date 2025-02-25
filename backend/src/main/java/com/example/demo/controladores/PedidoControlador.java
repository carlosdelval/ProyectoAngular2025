package com.example.demo.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.modelos.Cliente;
import com.example.demo.modelos.Pedido;
import com.example.demo.modelos.Plato;
import com.example.demo.repositorios.ClienteRepositorio;
import com.example.demo.repositorios.PedidoRepositorio;
import com.example.demo.repositorios.PlatoRepositorio;

@CrossOrigin
@RestController
@RequestMapping("/pedidos")
public class PedidoControlador {
    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    @Autowired
    private PlatoRepositorio platoRepo;
    @Autowired
    private ClienteRepositorio clienteRepo;

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

    // Método para obtener pedidos de un cliente específico
    @PostMapping(path = "/obtener1", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO getPedidosPorCliente(@RequestBody DTO soloId) {
        DTO respuesta = new DTO();
        Long idCliente = soloId.get("id") instanceof Number ? ((Number) soloId.get("id")).longValue() : null;
        List<Pedido> pedidos = pedidoRepositorio.findByClienteId(idCliente);

        if (!pedidos.isEmpty()) {
            List<DTO> listaPedidos = new ArrayList<>();
            for (Pedido p : pedidos) {
                DTO dtoPedido = new DTO();
                dtoPedido.put("id", p.getId());
                dtoPedido.put("fecha", p.getFecha_pedido());
                dtoPedido.put("total", p.getTotal());
                dtoPedido.put("cliente_id", p.getClienteId());
                dtoPedido.put("plato_id", p.getPlatoId());
                listaPedidos.add(dtoPedido);
            }
            respuesta.put("pedidos", listaPedidos);
        } else {
            respuesta.put("result", "no pedidos");
        }

        return respuesta;
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

    @PostMapping(path = "/obtener-plato", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO getPlato(@RequestBody DTO soloId) {
        DTO dtoPlato = new DTO();

        // Verifica que el ID recibido no sea nulo antes de intentar convertirlo
        if (soloId.get("id") == null) {
            dtoPlato.put("error", "ID del plato no proporcionado");
            return dtoPlato;
        }

        Long platoId;
        try {
            platoId = Long.parseLong(soloId.get("id").toString());
        } catch (NumberFormatException e) {
            dtoPlato.put("error", "ID del plato no válido");
            return dtoPlato;
        }

        // Busca el plato en la base de datos
        Plato p = platoRepo.findById(platoId).orElse(null);

        if (p != null) {
            dtoPlato.put("id", p.getId());
            dtoPlato.put("nombre", p.getNombre());
        } else {
            dtoPlato.put("error", "Plato no encontrado");
        }

        return dtoPlato;
    }

    @PostMapping(path = "/obtener-cliente", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO getClienteById(@RequestBody DTO soloId) {
        DTO dtoCliente = new DTO();
        Cliente c = clienteRepo.findById(Long.parseLong(soloId.get("id").toString())).orElse(null);
        if (c != null) {
            dtoCliente.put("nombre", c.getNombre());
        } else {
            dtoCliente.put("result", "fail");
        }
        return dtoCliente;
    }
}
