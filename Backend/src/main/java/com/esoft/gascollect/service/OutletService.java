package com.esoft.gascollect.service;

import com.esoft.gascollect.dto.OutletDTO;
import com.esoft.gascollect.dto.OutletReturnDTO;
import com.esoft.gascollect.entity.Outlet;
import com.esoft.gascollect.repository.OutletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OutletService {

    private final OutletRepository outletRepository;

    public OutletService(OutletRepository outletRepository) {
        this.outletRepository = outletRepository;
    }

    public OutletReturnDTO createOutlet(OutletDTO outletDTO) {
        Outlet outlet = new Outlet();
        outlet.setLocation(outletDTO.getLocation());
        outlet.setName(outletDTO.getName());
        outlet.setContactDetails(outletDTO.getContactDetails());
        outlet.setStock(outletDTO.getStock());
        Outlet savedOutlet = outletRepository.save(outlet);
        return mapToReturnDTO(savedOutlet);
    }

    public List<OutletReturnDTO> getAllOutlets() {
        List<Outlet> outlets = outletRepository.findAll();
        return outlets.stream()
                .map(this::mapToReturnDTO)
                .collect(Collectors.toList());
    }

    public OutletReturnDTO getOutletById(int id) {
        Optional<Outlet> outletOptional = outletRepository.findById(id);
        return outletOptional.map(this::mapToReturnDTO)
                .orElseThrow(() -> new RuntimeException("Outlet not found with id: " + id));
    }

    public OutletReturnDTO updateOutlet(int id, OutletDTO outletDTO) {
        // Find the existing outlet by ID
        Outlet outlet = outletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outlet not found with id: " + id));

        // Update only the fields that are provided in the DTO
        if (outletDTO.getLocation() != null) {
            outlet.setLocation(outletDTO.getLocation());
        }
        if (outletDTO.getName() != null) {
            outlet.setName(outletDTO.getName());
        }
        if (outletDTO.getContactDetails() != null) {
            outlet.setContactDetails(outletDTO.getContactDetails());
        }
        if (outletDTO.getStock() != null) {
            outlet.setStock(outletDTO.getStock());
        }

        // Save the updated outlet
        Outlet updatedOutlet = outletRepository.save(outlet);

        // Convert the updated outlet to a DTO and return it
        return mapToReturnDTO(updatedOutlet);
    }

    public void deleteOutlet(int id) {
        Outlet outlet = outletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outlet not found with id: " + id));
        outletRepository.delete(outlet);
    }

    private OutletReturnDTO mapToReturnDTO(Outlet outlet) {
        OutletReturnDTO outletReturnDTO = new OutletReturnDTO();
        outletReturnDTO.setId(outlet.getId());
        outletReturnDTO.setLocation(outlet.getLocation());
        outletReturnDTO.setName(outlet.getName());
        outletReturnDTO.setContactDetails(outlet.getContactDetails());
        outletReturnDTO.setStock(outlet.getStock());
        return outletReturnDTO;
    }
}
