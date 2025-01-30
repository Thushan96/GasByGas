package com.esoft.gascollect.service;

import com.esoft.gascollect.dto.GasDTO;
import com.esoft.gascollect.entity.Gas;
import com.esoft.gascollect.repository.GasRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GasService {
    private final GasRepository gasRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public GasService(final GasRepository gasRepository) {
        this.gasRepository = gasRepository;
    }


    public GasDTO createGas(GasDTO gasDTO) {
        Gas gas = new Gas();
        BeanUtils.copyProperties(gasDTO, gas);
        Gas savedGas = gasRepository.save(gas);
        GasDTO savedGasDTO = new GasDTO();
        BeanUtils.copyProperties(savedGas, savedGasDTO);
        return savedGasDTO;
    }

    public GasDTO getGasById(int id) {
        Gas gas = gasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gas not found with id: " + id));
        GasDTO gasDTO = new GasDTO();
        BeanUtils.copyProperties(gas, gasDTO);
        return gasDTO;
    }

    public List<GasDTO> getAllGases() {
        return gasRepository.findAll().stream().map(gas -> {
            GasDTO gasDTO = new GasDTO();
            BeanUtils.copyProperties(gas, gasDTO);
            return gasDTO;
        }).collect(Collectors.toList());
    }

    public GasDTO updateGas(int id, GasDTO gasDTO) {
        // Find the existing Gas by ID
        Gas gas = gasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gas not found with id: " + id));

        if (gasDTO.getCapacity() != 0) {
            gas.setCapacity(gasDTO.getCapacity());
        }
        if (gasDTO.getPrice() != 0) {
            gas.setPrice(gasDTO.getPrice());
        }
        if (gasDTO.getStock() != 0) {
            gas.setStock(gasDTO.getStock());
        }

        // Save the updated Gas
        Gas updatedGas = gasRepository.save(gas);

        // Convert the updated Gas to a DTO and return it
        GasDTO updatedGasDTO = new GasDTO();
        updatedGasDTO.setId(updatedGas.getId());
        updatedGasDTO.setCapacity(updatedGas.getCapacity());
        updatedGasDTO.setPrice(updatedGas.getPrice());
        updatedGasDTO.setStock(updatedGas.getStock());

        return updatedGasDTO;
    }

    public void deleteGas(int id) {
        Gas gas = gasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gas not found with id: " + id));
        gasRepository.delete(gas);
    }

    @Transactional
    public GasDTO updateStockToZero(int id) {
        // Fetch the gas entity by ID
        Gas gas = gasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gas entity not found for ID: " + id));

        // Update the stock to zero
        gas.setStock(0);

        // Save the updated entity
        Gas updatedGasEntity = gasRepository.save(gas);

        // Convert to DTO and return
        return objectMapper.convertValue(updatedGasEntity, GasDTO.class);
    }

}
