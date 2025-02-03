package com.esoft.gascollect.controller;

import com.esoft.gascollect.dto.OutletDTO;
import com.esoft.gascollect.dto.OutletReturnDTO;
import com.esoft.gascollect.service.OutletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/outlets")
public class OutletController {

    private final OutletService outletService;

    public OutletController(OutletService outletService) {
        this.outletService = outletService;
    }

    @PostMapping
    public ResponseEntity<OutletReturnDTO> createOutlet(@RequestBody OutletDTO outletDTO) {
        OutletReturnDTO outletReturnDTO = outletService.createOutlet(outletDTO);
        return ResponseEntity.ok(outletReturnDTO);
    }

    @GetMapping
    public ResponseEntity<List<OutletReturnDTO>> getAllOutlets() {
        List<OutletReturnDTO> outlets = outletService.getAllOutlets();
        return ResponseEntity.ok(outlets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutletReturnDTO> getOutletById(@PathVariable int id) {
        OutletReturnDTO outletReturnDTO = outletService.getOutletById(id);
        return ResponseEntity.ok(outletReturnDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OutletReturnDTO> updateOutlet(@PathVariable int id, @RequestBody OutletDTO outletDTO) {
        OutletReturnDTO outletReturnDTO = outletService.updateOutlet(id, outletDTO);
        return ResponseEntity.ok(outletReturnDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutlet(@PathVariable int id) {
        outletService.deleteOutlet(id);
        return ResponseEntity.noContent().build();
    }
}
