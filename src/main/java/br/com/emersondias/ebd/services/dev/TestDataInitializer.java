package br.com.emersondias.ebd.services.dev;

import br.com.emersondias.ebd.dtos.*;
import br.com.emersondias.ebd.dtos.location.CityDTO;
import br.com.emersondias.ebd.entities.Classroom;
import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.entities.enums.*;
import br.com.emersondias.ebd.services.interfaces.IAgeRangeService;
import br.com.emersondias.ebd.services.interfaces.IClassroomService;
import br.com.emersondias.ebd.services.interfaces.IPersonService;
import br.com.emersondias.ebd.services.interfaces.IUserService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static br.com.emersondias.ebd.utils.RandomUtils.*;
import static br.com.emersondias.ebd.utils.Utils.removeAccents;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Profile("test")
@Service
@RequiredArgsConstructor
public class TestDataInitializer {

    private final IUserService userService;
    private final IPersonService personService;
    private final IAgeRangeService ageRangeService;
    private final IClassroomService classroomService;
    private final Faker faker = new Faker();

    public void initializeTestData() {

        var teachers = seedTeachers();
        var students = seedStudents();
        var classrooms = seedClassrooms(teachers, students);
        var teacherUsers = seedUsersByPeeople(teachers);

        List<UserDTO> users = seedUsers();


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

    private List<ClassroomDTO> seedClassrooms(List<PersonDTO> teachers, List<PersonDTO> students) {
       return ageRangeService.findAll().stream()
                .map(ageRange -> {
                    var teachersGroup = new HashSet<PersonDTO>();
                    do {
                        teachersGroup.add(teachers.get(generateRandomInteger(0, teachers.size() - 1)));
                    } while (teachersGroup.size() < 3 && teachersGroup.size() != teachers.size());

                    var studentsGroup = students.stream()
                            .filter(student -> nonNull(student.getBirthdate()))
                            .filter(filterByAgeRange(ageRange))
                            .map(this::convertPersonToStudent)
                            .toList();
                    return ClassroomDTO.builder()
                            .name(ageRange.getName())
                            .ageRange(ageRange)
                            .teachers(teachersGroup.stream().map(this::convertPersonToTeacher).toList())
                            .students(studentsGroup)
                            .active(true)
                            .build();
                })
                .map(classroomService::create).toList();
    }

    private Predicate<PersonDTO> filterByAgeRange(AgeRangeDTO ageRange) {
        return student -> {
            var minAge = isNull(ageRange.getMinAge()) ? 0 : ageRange.getMinAge();
            var maxAge = isNull(ageRange.getMaxAge()) ? 999 : ageRange.getMaxAge();
            var age = Period.between(student.getBirthdate(), LocalDate.now()).getYears();
            return age >= minAge && age <= maxAge;
        };
    }

    private StudentDTO convertPersonToStudent(PersonDTO person) {
        return StudentDTO.builder()
                .academicPeriodStart(LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1))
                .academicPeriodEnd(LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31))
                .person(person)
                .active(true)
                .build();
    }

    private TeacherDTO convertPersonToTeacher(PersonDTO person) {
        return TeacherDTO.builder()
                .teachingPeriodStart(LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1))
                .teachingPeriodEnd(LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31))
                .person(person)
                .active(true)
                .build();
    }

    private List<UserDTO> seedUsersByPeeople(List<PersonDTO> people) {
        var users = people.stream().map(person ->
                        UserDTO.builder()
                                .email(person.getEmail())
                                .name(person.getName())
                                .password("Test@123")
                                .person(person)
                                .roles(person.getTypes().contains(PersonType.TEACHER) ? Set.of(UserRole.TEACHER) : Collections.emptySet())
                                .active(true)
                                .build()
                )
                .map(userService::create)
                .toList();
          return users;
    }

    private List<PersonDTO> seedTeachers() {
        return Arrays.asList(
                personService.create(generatePerson("Pedro Henrique", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Carla Menezes", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("João Pedro", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Ana Clara", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Ricardo Silva", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Patrícia Almeida", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Gustavo Costa", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Camila Ferreira", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Rafael Gomes", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Bianca Oliveira", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Thiago Rodrigues", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Fernanda Dias", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Bruno Lima", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Júlia Martins", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Felipe Cardoso", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Larissa Barbosa", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Marcelo Andrade", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Vanessa Pinto", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Diego Souza", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Gabriela Castro", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Rodrigo Oliveira", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Isabela Santos", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Vitor Moura", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Renata Farias", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("André Moreira", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Natália Costa", Gender.FEMALE, PersonType.TEACHER)),
                personService.create(generatePerson("Leandro Duarte", Gender.MALE, PersonType.TEACHER)),
                personService.create(generatePerson("Bruna Ribeiro", Gender.FEMALE, PersonType.TEACHER))
        );
    }

    private List<PersonDTO> seedStudents() {
        return Arrays.asList(
                personService.create(generatePerson("Lucas Almeida", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Mariana Souza", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Rafael Pereira", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Ana Beatriz Santos", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Gabriel Oliveira", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Júlia Costa", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Felipe Rodrigues", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Isabella Carvalho", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Matheus Lima", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Larissa Ferreira", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Pedro Henrique Martins", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Letícia Rocha", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Gustavo Silva", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Camila Mendes", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Vinícius Moreira", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Bruna Araújo", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Thiago Ribeiro", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Gabriela Fonseca", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Daniel Monteiro", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Amanda Nogueira", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Lucas Silva", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Ana Costa", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("João Oliveira", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Maria Souza", Gender.FEMALE, PersonType.STUDENT)),
                personService.create(generatePerson("Pedro Lima", Gender.MALE, PersonType.STUDENT)),
                personService.create(generatePerson("Fernanda Pereira", Gender.FEMALE, PersonType.STUDENT))
        );
    }

    private List<UserDTO> seedUsers() {
        List<UserDTO> users = new ArrayList<>();

        users.add(userService.create(UserDTO.builder().name("Emerson Dias").email("emersondias@hotmail.com")
                .password("Test@123").roles(Set.of(UserRole.ADMIN, UserRole.TEACHER)).active(true).build()));
        users.add(userService.create(UserDTO.builder().name("Testing").email("test@test.com").password("Test@123")
                .roles(Set.of(UserRole.TEACHER)).active(true).build()));

        return users;
    }

    private PersonDTO generatePerson(String name, Gender gender, PersonType... types) {
        var nameParts = removeAccents(name).toLowerCase().split(" ");
        var email = nameParts[0] + "." + nameParts[nameParts.length - 1] + "@" + chooseRandomElement("gmail.com", "hotmail.com", "yahoo.com.br", "msn.com");
        return PersonDTO.builder()
                .name(name)
                .email(email)
                .birthdate(generateRandomLocalDate(1950, 2015))
                .gender(gender)
                .educationLevel(chooseRandomElement(EducationLevel.values()))
                .maritalStatus(chooseRandomElement(MaritalStatus.values()))
                .phoneNumbers(generateRandomPhoneNumbers(generateRandomInteger(0, 3)))
                .types(Set.of(types))
                .address(generateRandomAddress())
                .active(true)
                .build();
    }

    private AddressDTO generateRandomAddress() {
        var address = AddressDTO.builder()
                .street(faker.address().streetName())
                .number(faker.address().streetAddressNumber())
                .complement(chooseRandomElement(true, false) ? faker.address().secondaryAddress() : null)
                .neighborhood(faker.address().cityName())
                .zipCode("8" + String.format("%07d", generateRandomInteger(0, 9999999)))
                .city(CityDTO.builder().id(chooseRandomElement(4100400L, 4105805L, 4106902L, 4107652L, 4119152L, 4125506L)).build())
                .active(true)
                .build();
        return address;
    }

    private List<PhoneNumberDTO> generateRandomPhoneNumbers(int quantity) {
        List<PhoneNumberDTO> phoneNumbers = new ArrayList<>();
        var areaCodesOptions = Arrays.asList("41", "42", "43", "44");
        var areaCodesOptionsWeights = Arrays.asList(7, 1, 1, 1);
        for (int i = 0; i < quantity; i++) {
            var areaCode = chooseWeightedRandomElement(areaCodesOptions, areaCodesOptionsWeights);
            var phoneNumber = chooseRandomElement("99", "98", "3") + String.format("%07d", generateRandomInteger(0, 9999999));
            phoneNumbers.add(
                    PhoneNumberDTO.builder()
                            .areaCode(areaCode)
                            .phoneNumber(phoneNumber)
                            .active(true)
                            .build()
            );
        }
        return phoneNumbers;
    }

}
