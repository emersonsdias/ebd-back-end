package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.ClassroomDTO;
import br.com.emersondias.ebd.entities.Classroom;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.ClassroomMapper;
import br.com.emersondias.ebd.mappers.StudentMapper;
import br.com.emersondias.ebd.mappers.TeacherMapper;
import br.com.emersondias.ebd.repositories.ClassrroomRepository;
import br.com.emersondias.ebd.services.interfaces.IClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements IClassroomService {

    private final ClassrroomRepository repository;

    @Override
    public ClassroomDTO create(ClassroomDTO classroomDTO) {
        requireNonNull(classroomDTO);
        classroomDTO.setId(null);
        classroomDTO.setActive(true);
        var classroomEntity = repository.save(ClassroomMapper.toEntity(classroomDTO));
        return ClassroomMapper.toDTO(classroomEntity);
    }

    @Override
    public ClassroomDTO update(ClassroomDTO classroomDTO) {
        requireNonNull(classroomDTO);
        requireNonNull(classroomDTO.getId());
        var classroomEntity = findEntityById(classroomDTO.getId());
        updateData(classroomEntity, classroomDTO);
        return null;
    }

    private void updateData(Classroom classroomEntity, ClassroomDTO classroomDTO) {
        classroomEntity.setName(classroomDTO.getName());
        classroomEntity.setActive(classroomDTO.isActive());
        classroomEntity.setTeachers(classroomDTO.getTeachers().stream().map(TeacherMapper::toEntity).toList());
        classroomEntity.setStudents(classroomDTO.getStudents().stream().map(StudentMapper::toEntity).toList());
    }

    @Override
    public void delete(Long id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Override
    public ClassroomDTO findById(Long id) {
        requireNonNull(id);
        return ClassroomMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<ClassroomDTO> findAll() {
        return repository.findAll().stream().map(ClassroomMapper::toDTO).toList();
    }

    private Classroom findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Classroom.class));
    }

}
