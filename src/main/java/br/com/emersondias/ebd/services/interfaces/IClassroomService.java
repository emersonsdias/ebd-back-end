package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.ClassroomDTO;

import java.util.List;

public interface IClassroomService {

    ClassroomDTO create(ClassroomDTO classroomDTO);

    ClassroomDTO update(ClassroomDTO classroomDTO);

    void delete(Long id);

    ClassroomDTO findById(Long id);

    List<ClassroomDTO> findAll();

}
