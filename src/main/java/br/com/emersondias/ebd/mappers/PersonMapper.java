package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.PersonDTO;
import br.com.emersondias.ebd.entities.Person;

public class PersonMapper {

    public static PersonDTO toDTO(Person entity) {
        return PersonDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .birthdate(entity.getBirthdate())
                .email(entity.getEmail())
                .gender(entity.getGender())
                .educationLevel(entity.getEducationLevel())
                .maritalStatus(entity.getMaritalStatus())
                .address(AddressMapper.toDTO(entity.getAddress()))
                .phoneNumbers(entity.getPhoneNumbers().stream().map(PhoneNumberMapper::toDTO).toList())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Person toEntity(PersonDTO dto) {
        var person = Person.builder()
                .id(dto.getId())
                .name(dto.getName())
                .birthdate(dto.getBirthdate())
                .email(dto.getEmail())
                .gender(dto.getGender())
                .educationLevel(dto.getEducationLevel())
                .maritalStatus(dto.getMaritalStatus())
                .address(AddressMapper.toEntity(dto.getAddress()))
                .active(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
        person.setPhoneNumbers(
                dto.getPhoneNumbers().stream()
                        .map(PhoneNumberMapper::toEntity)
                        .peek(phoneNumber -> phoneNumber.setPerson(person))
                        .toList()
        );
        return person;
    }

}
