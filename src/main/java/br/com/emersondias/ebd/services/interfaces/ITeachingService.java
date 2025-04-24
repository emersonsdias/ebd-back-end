package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.TeachingDTO;

import java.util.List;
import java.util.UUID;

public interface ITeachingService {

    List<TeachingDTO> findByTeacherPersonId(UUID personId);

}
