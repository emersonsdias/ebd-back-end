package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.AgeRangeDTO;

import java.util.List;

public interface IAgeRangeService {

    AgeRangeDTO create(AgeRangeDTO ageRangeDTO);

    AgeRangeDTO update(AgeRangeDTO ageRangeDTO);

    void delete(Long id);

    AgeRangeDTO findById(Long id);

    List<AgeRangeDTO> findAll();

}
