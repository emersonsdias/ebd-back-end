package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.ClassroomDTO;
import br.com.emersondias.ebd.dtos.SimpleClassroomDTO;
import br.com.emersondias.ebd.entities.Classroom;
import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.entities.Student;
import br.com.emersondias.ebd.entities.Teacher;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.ClassroomMapper;
import br.com.emersondias.ebd.mappers.StudentMapper;
import br.com.emersondias.ebd.mappers.TeacherMapper;
import br.com.emersondias.ebd.repositories.ClassroomRepository;
import br.com.emersondias.ebd.repositories.PersonRepository;
import br.com.emersondias.ebd.services.interfaces.IClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements IClassroomService {

    private final ClassroomRepository repository;
    private final PersonRepository personRepository;

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
        classroomEntity = repository.save(classroomEntity);
        return ClassroomMapper.toDTO(classroomEntity);
    }

    private void updateData(Classroom classroomEntity, ClassroomDTO classroomDTO) {
        classroomEntity.setName(classroomDTO.getName());
        classroomEntity.setActive(classroomDTO.isActive());
        classroomEntity.setTeachers(classroomDTO.getTeachers().stream().map(TeacherMapper::toEntity).collect(Collectors.toSet()));
        classroomEntity.setStudents(classroomDTO.getStudents().stream().map(StudentMapper::toEntity).collect(Collectors.toSet()));
    }

    @Override
    public void delete(Long id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ClassroomDTO findById(Long id) {
        requireNonNull(id);
        return ClassroomMapper.toDTO(findEntityById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ClassroomDTO> findAll() {
        return repository.findByActiveTrue().stream().map(ClassroomMapper::toDTO).toList();
    }

    @Transactional
    @Override
    public ClassroomDTO assignTeacher(Long classroomId, UUID personId) {
        requireNonNull(classroomId);
        requireNonNull(personId);
        var classroom = findEntityById(classroomId);
        var person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException(personId, Person.class));
        Teacher teacher = Teacher.builder()
                .person(person)
                .classroom(classroom)
                .active(true)
                .build();
        classroom.addTeacher(teacher);
        classroom = repository.save(classroom);
        return ClassroomMapper.toDTO(classroom);
    }

    @Transactional
    @Override
    public ClassroomDTO enrollStudent(Long classroomId, UUID personId) {
        requireNonNull(classroomId);
        requireNonNull(personId);
        var classroom = findEntityById(classroomId);
        var person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException(personId, Person.class));
        Student student = Student.builder()
                .person(person)
                .classroom(classroom)
                .active(true)
                .build();
        classroom.addStudent(student);
        classroom = repository.save(classroom);
        return ClassroomMapper.toDTO(classroom);
    }

    @Override
    public List<SimpleClassroomDTO> findByStudentsPersonId(UUID personId) {
        requireNonNull(personId);
        var classrooms = repository.findByStudentsPersonId(personId);
        return classrooms.stream().map(ClassroomMapper::toSimpleDTO).toList();
    }

    private Classroom findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Classroom.class));
    }

}
