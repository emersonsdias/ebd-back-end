package br.com.emersondias.ebd.dtos.filters;

import br.com.emersondias.ebd.entities.enums.LessonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class LessonFilterDTO {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Integer lessonNumber;
    private final LessonStatus lessonStatus;

}
