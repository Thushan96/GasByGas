package com.esoft.gascollect.controller;

import com.esoft.gascollect.dto.GasDTO;
import com.esoft.gascollect.service.GasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gas")
public class GasController {

    private final GasService gasService;

    @Autowired
    public GasController(GasService gasService) {
        this.gasService = gasService;
    }

    @PostMapping
    public ResponseEntity<GasDTO> createGas(@RequestBody GasDTO gasDTO) {
        GasDTO createdGas = gasService.createGas(gasDTO);
        return ResponseEntity.ok(createdGas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GasDTO> getGasById(@PathVariable int id) {
        GasDTO gas = gasService.getGasById(id);
        return ResponseEntity.ok(gas);
    }

    @GetMapping
    public ResponseEntity<List<GasDTO>> getAllGases() {
        List<GasDTO> gases = gasService.getAllGases();
        return ResponseEntity.ok(gases);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GasDTO> updateGas(@PathVariable int id, @RequestBody GasDTO gasDTO) {
        GasDTO updatedGas = gasService.updateGas(id, gasDTO);
        return ResponseEntity.ok(updatedGas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGas(@PathVariable int id) {
        gasService.deleteGas(id);
        return ResponseEntity.noContent().build();
    }
}
