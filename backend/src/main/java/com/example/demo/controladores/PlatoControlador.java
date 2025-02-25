package com.example.demo.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.example.demo.modelos.Plato;
import com.example.demo.repositorios.PlatoRepositorio;


@CrossOrigin
@RestController
@RequestMapping("/plato")
public class PlatoControlador {

    @Autowired
    PlatoRepositorio platoRepo;

    @GetMapping("/obtener")
    public List<DTO> getPlatos() {
        List<DTO> listPlatosDTO = new ArrayList<>();
        List<Plato> platos = platoRepo.findAll();
        for (Plato p : platos) {
            DTO dtoPlato = new DTO();
            dtoPlato.put("id", p.getId());
            dtoPlato.put("nombre", p.getNombre());
            dtoPlato.put("precio", p.getPrecio());
            dtoPlato.put("descripcion", p.getDescripcion());
            dtoPlato.put("img", p.getImg());
            dtoPlato.put("destacado", p.getDestacado());
            listPlatosDTO.add(dtoPlato);
        }
        return listPlatosDTO;
    }

    @GetMapping("/obtener/{id}")
    public DTO getPlatoById(@PathVariable Long id) {
        Plato p = platoRepo.findById(id).orElse(null);
        if (p != null) {
            DTO dtoPlato = new DTO();
            dtoPlato.put("id", p.getId());
            dtoPlato.put("nombre", p.getNombre());
            dtoPlato.put("precio", p.getPrecio());
            dtoPlato.put("descripcion", p.getDescripcion());
            dtoPlato.put("img", p.getImg());
            dtoPlato.put("destacado", p.getDestacado());
            return dtoPlato;
        } else {
            return null; // or handle the case where the Plato is not found
        }
    }

    @GetMapping("/obtener2")
    public List<DTO> getPlatosDestacados() {
        List<DTO> listPlatosDTO = new ArrayList<>();
        List<Plato> platos = platoRepo.findByDestacado(true);
        for (Plato p : platos) {
            DTO dtoPlato = new DTO();
            dtoPlato.put("id", p.getId());
            dtoPlato.put("nombre", p.getNombre());
            dtoPlato.put("precio", p.getPrecio());
            dtoPlato.put("descripcion", p.getDescripcion());
            dtoPlato.put("img", p.getImg());
            dtoPlato.put("destacado", p.getDestacado());
            listPlatosDTO.add(dtoPlato);
        }
        return listPlatosDTO;
    }

    @DeleteMapping(path = "/borrar1", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO deletePlato(@RequestBody DTO soloId) {
        DTO dtoMoto = new DTO();
        Plato p = platoRepo.findById(Long.parseLong(soloId.get("id").toString())).orElse(null);
        if (p != null) {
            platoRepo.delete(p);
            dtoMoto.put("borrado", "ok");
        } else {
            dtoMoto.put("borrado", "fail");
        }
        return dtoMoto;
    }

    @PostMapping("/addnew")
    public void addNewPlato(@RequestBody Plato moto) {
        platoRepo.save(moto);
    }

}