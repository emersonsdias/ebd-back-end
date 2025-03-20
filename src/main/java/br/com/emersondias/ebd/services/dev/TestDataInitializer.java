package br.com.emersondias.ebd.services.dev;

import br.com.emersondias.ebd.dtos.PersonDTO;
import br.com.emersondias.ebd.dtos.PhoneNumberDTO;
import br.com.emersondias.ebd.dtos.UserDTO;
import br.com.emersondias.ebd.entities.enums.*;
import br.com.emersondias.ebd.services.interfaces.IPersonService;
import br.com.emersondias.ebd.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Set;

@Profile("test")
@Service
@RequiredArgsConstructor
public class TestDataInitializer {

    private final IUserService userService;
    private final IPersonService personService;

    public void initializeTestData() {

        var user1 = UserDTO.builder()
                .name("Emerson Dias")
                .email("emersondias@hotmail.com")
                .password("Test@123")
                .roles(Set.of(UserRole.ADMIN, UserRole.TEACHER))
                .active(true)
                .build();
        userService.create(user1);

        var user2 = UserDTO.builder()
                .name("Testing")
                .email("test@test.com")
                .password("Test@123")
                .roles(Set.of(UserRole.TEACHER))
                .active(true)
                .build();
        userService.create(user2);


        var personDTO = PersonDTO.builder()
                .name("Data Test")
                .birthdate(LocalDate.of(1990, Month.JANUARY, 1))
                .email("test@test.com")
                .gender(Gender.MALE)
                .educationLevel(EducationLevel.POSTGRADUATE)
                .maritalStatus(MaritalStatus.MARRIED)
                .phoneNumbers(Arrays.asList(
                        PhoneNumberDTO.builder().areaCode("41").phoneNumber("988888888").build(),
                        PhoneNumberDTO.builder().areaCode("41").phoneNumber("999999999").build()
                ))
                .types(Set.of(PersonType.TEACHER, PersonType.STUDENT))
                .active(true)
                .build();

        personService.create(personDTO);

    }

}
