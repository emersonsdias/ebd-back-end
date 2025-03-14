package br.com.emersondias.ebd.mappers;

import br.com.emersondias.ebd.dtos.PersonDTO;
import br.com.emersondias.ebd.entities.Person;

import static java.util.Objects.nonNull;

public class PersonMapper {

    public static PersonDTO toDTO(Person entity) {
        var personDTO = PersonDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .birthdate(entity.getBirthdate())
                .email(entity.getEmail())
                .gender(entity.getGender())
                .educationLevel(entity.getEducationLevel())
                .maritalStatus(entity.getMaritalStatus())
                .phoneNumbers(entity.getPhoneNumbers().stream().map(PhoneNumberMapper::toDTO).toList())
                .types(entity.getTypes())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        if (nonNull(entity.getAddress())) {
            personDTO.setAddress(AddressMapper.toDTO(entity.getAddress()));
        }

        return personDTO;
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
                .types(dto.getTypes())
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
        if (nonNull(dto.getAddress())) {
            person.setAddress(AddressMapper.toEntity(dto.getAddress()));
        }
        return person;
    }

}
