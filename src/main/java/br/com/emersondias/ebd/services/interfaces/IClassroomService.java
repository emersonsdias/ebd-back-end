package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.ClassroomDTO;
import br.com.emersondias.ebd.dtos.SimpleClassroomDTO;

import java.util.List;
import java.util.UUID;

public interface IClassroomService {

    ClassroomDTO create(ClassroomDTO classroomDTO);

    ClassroomDTO update(ClassroomDTO classroomDTO);

    void delete(Long id);

    ClassroomDTO findById(Long id);

    List<ClassroomDTO> findAll();

    ClassroomDTO assignTeacher(Long classroomId, UUID personId);

    ClassroomDTO enrollStudent(Long classroomId, UUID personId);

    List<SimpleClassroomDTO> findByStudentsPersonId(UUID personId);
}
