package br.com.emersondias.ebd.dtos;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Getter
public class PersonReportDTO implements Serializable {

    private final PersonDTO person;
    private final List<ClassroomAttendanceDTO> attendancesByClassroom;
    private final Integer totalLessons;
    private final Integer attendedLessons;
    private final Integer missedLessons;

    public PersonReportDTO(PersonDTO person, List<ClassroomAttendanceDTO> attendancesByClassroom) {
        this.person = person;
        this.attendancesByClassroom = nonNull(attendancesByClassroom) ? attendancesByClassroom : new ArrayList<>();
        this.totalLessons = attendancesByClassroom
                .stream()
                .map(ClassroomAttendanceDTO::getAttendances)
                .mapToInt(List::size)
                .sum();
        this.attendedLessons = (int) attendancesByClassroom
                .stream()
                .map(ClassroomAttendanceDTO::getAttendances)
                .flatMap(List::stream)
                .filter(AttendanceDTO::isPresent)
                .count();
        this.missedLessons = this.totalLessons - this.attendedLessons;
    }

}
