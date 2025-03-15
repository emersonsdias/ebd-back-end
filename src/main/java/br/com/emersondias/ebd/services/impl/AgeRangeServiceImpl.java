package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.AgeRangeDTO;
import br.com.emersondias.ebd.entities.AgeRange;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.AgeRangeMapper;
import br.com.emersondias.ebd.repositories.AgeRangeRepository;
import br.com.emersondias.ebd.services.interfaces.IAgeRangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class AgeRangeServiceImpl implements IAgeRangeService {

    private final AgeRangeRepository repository;

    @Transactional
    @Override
    public AgeRangeDTO create(AgeRangeDTO ageRangeDTO) {
        requireNonNull(ageRangeDTO);
        ageRangeDTO.setId(null);
        ageRangeDTO.setActive(true);
        AgeRange ageRangeEntity = repository.save(AgeRangeMapper.toEntity(ageRangeDTO));
        return AgeRangeMapper.toDTO(ageRangeEntity);
    }

    @Transactional
    @Override
    public AgeRangeDTO update(AgeRangeDTO ageRangeDTO) {
        requireNonNull(ageRangeDTO);
        requireNonNull(ageRangeDTO.getId());
        AgeRange ageRangeEntity = findEntityById(ageRangeDTO.getId());
        updateData(ageRangeEntity, ageRangeDTO);
        ageRangeEntity = repository.save(ageRangeEntity);
        return AgeRangeMapper.toDTO(ageRangeEntity);
    }

    private void updateData(AgeRange ageRangeEntity, AgeRangeDTO ageRangeDTO) {
        ageRangeEntity.setName(ageRangeDTO.getName());
        ageRangeEntity.setMinAge(ageRangeDTO.getMinAge());
        ageRangeEntity.setMaxAge(ageRangeDTO.getMaxAge());
        ageRangeEntity.setActive(ageRangeDTO.isActive());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public AgeRangeDTO findById(Long id) {
        requireNonNull(id);
        return AgeRangeMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<AgeRangeDTO> findAll() {
        return repository.findByActiveTrue().stream().map(AgeRangeMapper::toDTO).toList();
    }

    private AgeRange findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, AgeRange.class));
    }
}
