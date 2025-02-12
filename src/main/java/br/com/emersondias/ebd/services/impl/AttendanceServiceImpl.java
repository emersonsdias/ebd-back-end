package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.AttendanceDTO;
import br.com.emersondias.ebd.mappers.AttendanceMapper;
import br.com.emersondias.ebd.repositories.AttendanceRepository;
import br.com.emersondias.ebd.services.interfaces.IAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements IAttendanceService {

    private final AttendanceRepository repository;

    @Override
    public List<AttendanceDTO> findByStudentPersonId(UUID personId) {
        return repository.findByStudentPersonId(personId).stream().map(AttendanceMapper::toDTO).toList();
    }
}
