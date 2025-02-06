package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.PersonDTO;

import java.util.List;
import java.util.UUID;

public interface IPersonService {

    PersonDTO create(PersonDTO personDTO);

    PersonDTO update(PersonDTO personDTO);

    void delete(UUID id);

    PersonDTO findById(UUID id);

    List<PersonDTO> findAll();
}
