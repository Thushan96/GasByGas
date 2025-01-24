package com.esoft.gascollect.service;

import com.esoft.gascollect.dto.GasDTO;
import com.esoft.gascollect.entity.Gas;
import com.esoft.gascollect.repository.GasRepository;
import lombok.extern.slf4j.Slf4j;
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
        Gas gas = gasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gas not found with id: " + id));
        BeanUtils.copyProperties(gasDTO, gas, "id");
        Gas updatedGas = gasRepository.save(gas);
        GasDTO updatedGasDTO = new GasDTO();
        BeanUtils.copyProperties(updatedGas, updatedGasDTO);
        return updatedGasDTO;
    }

    public void deleteGas(int id) {
        Gas gas = gasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gas not found with id: " + id));
        gasRepository.delete(gas);
    }


}
