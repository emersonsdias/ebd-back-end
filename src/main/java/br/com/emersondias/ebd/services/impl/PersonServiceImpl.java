package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.AddressDTO;
import br.com.emersondias.ebd.dtos.PersonDTO;
import br.com.emersondias.ebd.entities.Address;
import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.CityMapper;
import br.com.emersondias.ebd.mappers.PersonMapper;
import br.com.emersondias.ebd.mappers.PhoneNumberMapper;
import br.com.emersondias.ebd.repositories.PersonRepository;
import br.com.emersondias.ebd.services.interfaces.IPersonService;
import br.com.emersondias.ebd.services.interfaces.IReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements IPersonService {

    private final PersonRepository repository;
    private final IReportService reportService;

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
        updateAddress(personEntity.getAddress(), personDTO.getAddress());
    }

    private void updateAddress(Address addressEntity, AddressDTO addressDTO) {
        addressEntity.setStreet(addressDTO.getStreet());
        addressEntity.setNumber(addressDTO.getNumber());
        addressEntity.setComplement(addressDTO.getComplement());
        addressEntity.setNeighborhood(addressDTO.getNeighborhood());
        addressEntity.setZipCode(addressDTO.getZipCode());
        addressEntity.setCity(CityMapper.toEntity(addressDTO.getCity()));
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

    @Override
    public byte[] generatePersonPdf(UUID id) {
        var person = findEntityById(id);
        Map<String, Object> params = new HashMap<>();
        params.put("id", person.getId());
        params.put("name", person.getName());
        params.put("birthdate", person.getBirthdate());
        params.put("email", person.getEmail());

        try {
            return reportService.generatePdf("person_registration", params);
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Person findEntityById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Person.class));
    }


}
