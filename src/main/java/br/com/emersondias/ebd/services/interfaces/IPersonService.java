package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.PersonDTO;
import br.com.emersondias.ebd.dtos.PersonReportDTO;
import br.com.emersondias.ebd.entities.enums.PersonType;

import java.util.List;
import java.util.UUID;

public interface IPersonService {

    PersonDTO create(PersonDTO personDTO);

    PersonDTO update(PersonDTO personDTO);

    void delete(UUID id);

    PersonDTO findById(UUID id);

    List<PersonDTO> findAll();

    PersonReportDTO generatePersonReport(UUID id);

    byte[] generatePersonReportPdf(UUID id);

    List<PersonDTO> findByPersonType(List<PersonType> types);
}
