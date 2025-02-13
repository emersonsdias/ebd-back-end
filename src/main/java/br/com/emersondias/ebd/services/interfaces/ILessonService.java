package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.LessonDTO;

import java.util.List;

public interface ILessonService {

    LessonDTO create(LessonDTO lessonDTO);

    LessonDTO update(LessonDTO lessonDTO);

    void delete(Long id);

    LessonDTO findById(Long id);

    List<LessonDTO> findAll();

}
