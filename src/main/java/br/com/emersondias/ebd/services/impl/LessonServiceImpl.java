package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.dtos.filters.LessonFilterDTO;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.LessonMapper;
import br.com.emersondias.ebd.repositories.LessonRepository;
import br.com.emersondias.ebd.repositories.specifications.LessonSpecification;
import br.com.emersondias.ebd.services.interfaces.ILessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements ILessonService {

    private final LessonRepository repository;

    @Transactional
    @Override
    public LessonDTO create(LessonDTO lessonDTO) {
        requireNonNull(lessonDTO);
        lessonDTO.setId(null);
        Lesson lessonEntity = repository.save(LessonMapper.toEntity(lessonDTO));
        return LessonMapper.toDTO(lessonEntity);
    }

    @Transactional
    @Override
    public LessonDTO update(LessonDTO lessonDTO) {
        requireNonNull(lessonDTO);
        requireNonNull(lessonDTO.getId());
        Lesson lessonEntity = findEntityById(lessonDTO.getId());
        lessonEntity.updateFrom(requireNonNull(LessonMapper.toEntity(lessonDTO)));
        lessonEntity = repository.save(lessonEntity);
        return LessonMapper.toDTO(lessonEntity);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public LessonDTO findById(Long id) {
        requireNonNull(id);
        return LessonMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<LessonDTO> findAll() {
        return repository.findAll().stream().map(LessonMapper::toDTO).toList();
    }

    @Override
    public List<LessonDTO> findByPeriod(LocalDate startDate, LocalDate endDate) {
        if (isNull(startDate)) {
            startDate = (nonNull(endDate) ? endDate : LocalDate.now()).minusDays(15);
        }
        if (isNull(endDate)) {
            endDate = startDate.plusDays(15);
        }
        return repository.findByActiveTrueAndDateBetween(startDate, endDate).stream().map(LessonMapper::toDTO).toList();
    }

    @Override
    public List<LessonDTO> findByIds(List<Long> ids) {
        requireNonNull(ids);
        return repository.findAllById(ids).stream().map(LessonMapper::toDTO).toList();
    }

    @Override
    public List<LessonDTO> findByFilter(LessonFilterDTO filter) {
        requireNonNull(filter);
        var spec = LessonSpecification.withFilter(filter);
        List<Lesson> lessons = repository.findAll(spec);

        return lessons.stream().map(LessonMapper::toDTO).toList();
    }

    private Lesson findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
    }
}
