package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.PersonDTO;
import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.PersonMapper;
import br.com.emersondias.ebd.mappers.PhoneNumberMapper;
import br.com.emersondias.ebd.repositories.PersonRepository;
import br.com.emersondias.ebd.services.interfaces.IPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements IPersonService {

    private final PersonRepository repository;

    @Transactional
    @Override
    public PersonDTO create(PersonDTO personDTO) {
        requireNonNull(personDTO);
        personDTO.setId(null);
        personDTO.setActive(true);
        var personEntity = PersonMapper.toEntity(personDTO);
        personEntity = repository.save(personEntity);
        return PersonMapper.toDTO(personEntity);
    }

    @Transactional
    @Override
    public PersonDTO update(PersonDTO personDTO) {
        requireNonNull(personDTO);
        requireNonNull(personDTO.getId());
        var personEntity = findEntityById(personDTO.getId());
        updateData(personEntity, personDTO);
        personEntity = repository.save(personEntity);
        return PersonMapper.toDTO(personEntity);
    }

    private void updateData(Person personEntity, PersonDTO personDTO) {
        personEntity.setName(personDTO.getName());
        personEntity.setBirthdate(personDTO.getBirthdate());
        personEntity.setEmail(personDTO.getEmail());
        personEntity.setActive(personDTO.isActive());
        personEntity.setPhoneNumbers(personDTO.getPhoneNumbers().stream().map(PhoneNumberMapper::toEntity).toList());
    }


    @Override
    public void delete(UUID id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Override
    public PersonDTO findById(UUID id) {
        requireNonNull(id);
        return PersonMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<PersonDTO> findAll() {
        return repository.findAll().stream().map(PersonMapper::toDTO).toList();
    }

    private Person findEntityById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Person.class));
    }


}
