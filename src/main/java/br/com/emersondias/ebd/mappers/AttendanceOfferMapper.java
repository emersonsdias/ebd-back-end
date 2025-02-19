package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.AttendanceOfferDTO;
import br.com.emersondias.ebd.entities.Attendance;
import br.com.emersondias.ebd.entities.AttendanceOffer;

import java.util.Optional;

public class AttendanceOfferMapper {

    public static AttendanceOfferDTO toDTO(AttendanceOffer entity) {
        return AttendanceOfferDTO.builder()
                .id(entity.getId())
                .attendanceId(Optional.ofNullable(entity.getAttendance()).map(Attendance::getId).orElse(null))
                .offer(OfferMapper.toDTO(entity.getOffer()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static AttendanceOffer toEntity(AttendanceOfferDTO dto) {
        return AttendanceOffer.builder()
                .id(dto.getId())
                .attendance(Attendance.builder().id(dto.getId()).build())
                .offer(OfferMapper.toEntity(dto.getOffer()))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
