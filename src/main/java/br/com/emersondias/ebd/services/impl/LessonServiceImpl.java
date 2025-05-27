package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.dtos.filters.LessonFilterDTO;
import br.com.emersondias.ebd.entities.Lesson;
import br.com.emersondias.ebd.entities.enums.UserRole;
import br.com.emersondias.ebd.exceptions.ReportGenerationException;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.LessonMapper;
import br.com.emersondias.ebd.repositories.LessonRepository;
import br.com.emersondias.ebd.repositories.specifications.LessonSpecification;
import br.com.emersondias.ebd.security.utils.SecurityUtils;
import br.com.emersondias.ebd.services.interfaces.ILessonService;
import br.com.emersondias.ebd.services.interfaces.IReportService;
import br.com.emersondias.ebd.utils.LogHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements ILessonService {

    private static final LogHelper LOG = LogHelper.getInstance();

    private final LessonRepository repository;
    private final IReportService reportService;

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

    @Override
    public byte[] generateLessonUnitReportPdf(Integer lessonNumber, LocalDate startDate, LocalDate endDate) {
        requireNonNull(lessonNumber);
        requireNonNull(startDate);
        requireNonNull(endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("lesson_number", lessonNumber);
        params.put("start_date", Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        params.put("end_date", Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        try {
            return reportService.generatePdf("lesson_report/lesson_report", params);
        } catch (ReportGenerationException e) {
            var error = "Failed to build lesson report pdf, lesson number: '" + lessonNumber + "'";
            LOG.error(error);
            throw new RuntimeException(error, e);
        }
    }

    @Override
    public List<LessonDTO> findAllConsideringUser() {
        var authenticatedUser = SecurityUtils.getAuthenticatedUser();

        assert authenticatedUser != null;
        if (authenticatedUser.hasRole(UserRole.ADMIN)) {
            return findAll();
        }

        List<Lesson> lessons = repository.findLessonsByTeacherPersonId(authenticatedUser.getPersonId());

        return lessons.stream().map(LessonMapper::toDTO).toList();
    }

    @Override
    public List<LessonDTO> findByFilterConsideringUser(LessonFilterDTO filter) {
        requireNonNull(filter);

        var authenticatedUser = SecurityUtils.getAuthenticatedUser();
        assert authenticatedUser != null;
        if (authenticatedUser.hasRole(UserRole.ADMIN)) {
            return findByFilter(filter);
        }

        var spec = LessonSpecification.withFilterAndAuthenticatedPersonId(filter, authenticatedUser.getPersonId());
        List<Lesson> lessons = repository.findAll(spec);

        return lessons.stream().map(LessonMapper::toDTO).toList();
    }

    private Lesson findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
    }
}
