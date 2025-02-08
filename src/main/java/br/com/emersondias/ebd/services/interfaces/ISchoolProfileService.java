package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.SchoolProfileDTO;

import java.util.List;

public interface ISchoolProfileService {

    SchoolProfileDTO create(SchoolProfileDTO schoolProfileDTO);

    SchoolProfileDTO update(SchoolProfileDTO schoolProfileDTO);

    void delete(Long id);

    SchoolProfileDTO findById(Long id);

    List<SchoolProfileDTO> findAll();

}
