package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.entities.Classroom;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.AttendanceMapper;
import br.com.emersondias.ebd.mappers.LessonMapper;
import br.com.emersondias.ebd.mappers.TeachingMapper;
import br.com.emersondias.ebd.mappers.VisitorMapper;
import br.com.emersondias.ebd.repositories.LessonRepository;
import br.com.emersondias.ebd.services.interfaces.ILessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements ILessonService {

    private final LessonRepository repository;

    @Override
    public LessonDTO create(LessonDTO lessonDTO) {
        requireNonNull(lessonDTO);
        lessonDTO.setId(null);
        lessonDTO.setActive(true);
        Lesson lessonEntity = repository.save(LessonMapper.toEntity(lessonDTO));
        return LessonMapper.toDTO(lessonEntity);
    }

    @Override
    public LessonDTO update(LessonDTO lessonDTO) {
        requireNonNull(lessonDTO);
        requireNonNull(lessonDTO.getId());
        Lesson lessonEntity = findEntityById(lessonDTO.getId());
        updateData(lessonEntity, lessonDTO);
        lessonEntity = repository.save(lessonEntity);
        return LessonMapper.toDTO(lessonEntity);
    }

    private void updateData(Lesson lessonEntity, LessonDTO lessonDTO) {
        lessonEntity.setLessonNumber(lessonDTO.getLessonNumber());
        lessonEntity.setLessonDate(lessonDTO.getLessonDate());
        lessonEntity.setNotes(lessonDTO.getNotes());
        lessonEntity.setClassroom(Classroom.builder().id(lessonDTO.getClassroomId()).build());
        lessonEntity.setVisitors(lessonDTO.getVisitors().stream().map(VisitorMapper::toEntity).toList());
        lessonEntity.setAttendances(lessonDTO.getAttendances().stream().map(AttendanceMapper::toEntity).toList());
        lessonEntity.setTeachings(lessonDTO.getTeachings().stream().map(TeachingMapper::toEntity).toList());
        lessonEntity.setActive(lessonDTO.isActive());
    }

    @Override
    public void delete(Long id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Override
    public LessonDTO findById(Long id) {
        requireNonNull(id);
        return LessonMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<LessonDTO> findAll() {
        return repository.findByActiveTrue().stream().map(LessonMapper::toDTO).toList();
    }

    private Lesson findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
    }
}
