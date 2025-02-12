package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.AttendanceDTO;

import java.util.List;
import java.util.UUID;

public interface IAttendanceService {

    List<AttendanceDTO> findByStudentPersonId(UUID personId);
}
