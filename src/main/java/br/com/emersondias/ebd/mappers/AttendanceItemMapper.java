package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.AttendanceItemDTO;
import br.com.emersondias.ebd.entities.Attendance;
import br.com.emersondias.ebd.entities.AttendanceItem;

import java.util.Optional;

public class AttendanceItemMapper {

    public static AttendanceItemDTO toDTO(AttendanceItem entity) {
        return AttendanceItemDTO.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .attendanceId(Optional.ofNullable(entity.getAttendance()).map(Attendance::getId).orElse(null))
                .item(Optional.ofNullable(entity.getItem()).map(ItemMapper::toDTO).orElse(null))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static AttendanceItem toEntity(AttendanceItemDTO dto) {
        return AttendanceItem.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .attendance(Attendance.builder().id(dto.getAttendanceId()).build())
                .item(Optional.ofNullable(dto.getItem()).map(ItemMapper::toEntity).orElse(null))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
