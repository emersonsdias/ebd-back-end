package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.TeachingDTO;
import br.com.emersondias.ebd.mappers.TeachingMapper;
import br.com.emersondias.ebd.repositories.TeachingRepository;
import br.com.emersondias.ebd.services.interfaces.ITeachingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class TeachingServiceImpl implements ITeachingService {

    private final TeachingRepository repository;

    @Override
    public List<TeachingDTO> findByTeacherPersonId(UUID personId) {
        requireNonNull(personId);
        return repository.findByTeacherPersonId(personId).stream().map(TeachingMapper::toDTO).toList();
    }

}
