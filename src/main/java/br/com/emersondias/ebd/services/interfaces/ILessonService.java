package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.dtos.filters.LessonFilterDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ILessonService {

    LessonDTO create(LessonDTO lessonDTO);

    LessonDTO update(LessonDTO lessonDTO);

    void delete(Long id);

    LessonDTO findById(Long id);

    List<LessonDTO> findAll();

    List<LessonDTO> findByPeriod(LocalDate startDate, LocalDate endDate);

    List<LessonDTO> findByIds(List<Long> ids);

    List<LessonDTO> findByFilter(LessonFilterDTO filter);

    byte[] generateLessonUnitReportPdf(Integer lessonNumber, LocalDate startDate, LocalDate endDate);

}
