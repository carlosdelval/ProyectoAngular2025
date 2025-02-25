package com.example.demo.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.example.demo.modelos.Cocinero;
import com.example.demo.modelos.Plato;
import com.example.demo.repositorios.CocineroRepositorio;

@CrossOrigin
@RestController
@RequestMapping("/cocinero")
public class CocineroControlador {

    @Autowired
    CocineroRepositorio cocineroRepo;

    @Transactional
    @GetMapping("/obtener")
    public List<DTO> getCocineros() {
        List<DTO> listCocinerosDTO = new ArrayList<>();
        List<Cocinero> cocineros = cocineroRepo.findAll();

        for (Cocinero c : cocineros) {
            DTO dtoCocinero = new DTO();
            dtoCocinero.put("id", c.getId());
            dtoCocinero.put("nombre", c.getNombre());
            dtoCocinero.put("especialidad", c.getEspecialidad());

            List<DTO> platosDTO = new ArrayList<>();
            for (Plato p : c.getPlatos()) { // Asegúrate de tener una relación bien mapeada
                DTO dtoPlato = new DTO();
                dtoPlato.put("id", p.getId());
                dtoPlato.put("nombre", p.getNombre());
                platosDTO.add(dtoPlato);
            }

            dtoCocinero.put("platos", platosDTO);
            listCocinerosDTO.add(dtoCocinero);
        }
        return listCocinerosDTO;
    }

    @PostMapping(path = "/obtener1", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO getCocinero(@RequestBody DTO soloId) {
        DTO dtoCocinero = new DTO();
        Cocinero c = cocineroRepo.findById(Long.parseLong(soloId.get("id").toString())).orElse(null);
        if (c != null) {
            dtoCocinero.put("id", c.getId());
            dtoCocinero.put("nombre", c.getNombre());
            dtoCocinero.put("especialidad", c.getEspecialidad());
        } else {
            dtoCocinero.put("result", "fail");
        }
        return dtoCocinero;
    }

    @DeleteMapping(path = "/borrar1", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DTO deleteCocinero(@RequestBody DTO soloId) {
        DTO dtoCocinero = new DTO();
        Cocinero c = cocineroRepo.findById(Long.parseLong(soloId.get("id").toString())).orElse(null);
        if (c != null) {
            cocineroRepo.delete(c);
            dtoCocinero.put("borrado", "ok");
        } else {
            dtoCocinero.put("borrado", "fail");
        }
        return dtoCocinero;
    }

    @PostMapping("/addnew")
    public void addNewCocinero(@RequestBody Cocinero cocinero) {
        cocineroRepo.save(cocinero);
    }
}
