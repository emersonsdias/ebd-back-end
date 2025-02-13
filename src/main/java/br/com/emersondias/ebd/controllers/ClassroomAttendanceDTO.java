package br.com.emersondias.ebd.controllers;

import br.com.emersondias.ebd.dtos.AttendanceDTO;
import br.com.emersondias.ebd.dtos.SimpleClassroomDTO;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Getter
public class ClassroomAttendanceDTO implements Serializable {

    private final SimpleClassroomDTO classroom;
    private final List<AttendanceDTO> attendances;
    private final Integer totalLessons;
    private final Integer attendedLessons;
    private final Integer missedLessons;

    public ClassroomAttendanceDTO(SimpleClassroomDTO classroom, List<AttendanceDTO> attendances) {
        this.classroom = classroom;
        this.attendances = nonNull(attendances) ? attendances : new ArrayList<>();
        this.totalLessons = this.attendances.size();
        this.attendedLessons = (int) this.attendances.stream().filter(AttendanceDTO::isPresent).count();
        this.missedLessons = this.totalLessons - this.attendedLessons;
    }

    public Double getAttendanceRate() {
        if (this.totalLessons == 0) {
            return 0.0;
        }
        return ((double) this.attendedLessons / this.totalLessons) * 100;
    }
}
