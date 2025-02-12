package br.com.emersondias.ebd.dtos;

import br.com.emersondias.ebd.controllers.ClassroomAttendanceDTO;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Getter
public class PersonReportDTO implements Serializable {

    private final PersonDTO person;
    private final Map<Long, ClassroomAttendanceDTO> attendanceByClassroom;
    private final Integer totalLessons;
    private final Integer attendedLessons;
    private final Integer missedLessons;

    public PersonReportDTO(PersonDTO person, Map<Long, ClassroomAttendanceDTO> attendanceByClassroom) {
        this.person = person;
        this.attendanceByClassroom = nonNull(attendanceByClassroom) ? attendanceByClassroom : new HashMap<>();
        this.totalLessons = attendanceByClassroom.values()
                .stream()
                .map(ClassroomAttendanceDTO::getAttendances)
                .mapToInt(List::size)
                .sum();
        this.attendedLessons = (int) attendanceByClassroom.values()
                .stream()
                .map(ClassroomAttendanceDTO::getAttendances)
                .flatMap(List::stream)
                .filter(AttendanceDTO::isPresent)
                .count();
        this.missedLessons = this.totalLessons - this.attendedLessons;
    }

}
