package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.SchoolProfileDTO;
import br.com.emersondias.ebd.entities.SchoolProfile;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.AddressMapper;
import br.com.emersondias.ebd.mappers.SchoolProfileMapper;
import br.com.emersondias.ebd.repositories.SchoolProfileRepository;
import br.com.emersondias.ebd.services.interfaces.ISchoolProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class SchoolProfileServiceImpl implements ISchoolProfileService {

    private final SchoolProfileRepository repository;

    @Transactional
    @Override
    public SchoolProfileDTO create(SchoolProfileDTO schoolProfileDTO) {
        requireNonNull(schoolProfileDTO);
        schoolProfileDTO.setId(null);
        schoolProfileDTO.setActive(true);
        SchoolProfile schoolProfileEntity = repository.save(SchoolProfileMapper.toEntity(schoolProfileDTO));
        return SchoolProfileMapper.toDTO(schoolProfileEntity);
    }

    @Transactional
    @Override
    public SchoolProfileDTO update(SchoolProfileDTO schoolProfileDTO) {
        requireNonNull(schoolProfileDTO);
        requireNonNull(schoolProfileDTO.getId());
        SchoolProfile schoolProfileEntity = findEntityById(schoolProfileDTO.getId());
        updateData(schoolProfileEntity, schoolProfileDTO);
        schoolProfileEntity = repository.save(schoolProfileEntity);
        return SchoolProfileMapper.toDTO(schoolProfileEntity);
    }

    private void updateData(SchoolProfile schoolProfileEntity, SchoolProfileDTO schoolProfileDTO) {
        schoolProfileEntity.setName(schoolProfileDTO.getName());
        schoolProfileEntity.setSubtitle(schoolProfileDTO.getSubtitle());
        schoolProfileEntity.setActive(schoolProfileDTO.isActive());
        schoolProfileEntity.setAddress(AddressMapper.toEntity(schoolProfileDTO.getAddress()));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public SchoolProfileDTO findById(Long id) {
        requireNonNull(id);
        return SchoolProfileMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<SchoolProfileDTO> findAll() {
        return repository.findAll().stream().map(SchoolProfileMapper::toDTO).toList();
    }

    private SchoolProfile findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, SchoolProfile.class));
    }
}
