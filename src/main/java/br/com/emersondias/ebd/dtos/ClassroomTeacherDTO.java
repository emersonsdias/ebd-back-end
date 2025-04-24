package br.com.emersondias.ebd.dtos;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class ClassroomTeacherDTO implements Serializable {

    private final SimpleClassroomDTO classroom;
    private final List<TeachingDTO> teachings;

    public ClassroomTeacherDTO(SimpleClassroomDTO classroom, List<TeachingDTO> teachings) {
        this.classroom = classroom;
        this.teachings = teachings;
    }
}
