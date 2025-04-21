package br.com.emersondias.ebd.services.dev;

import br.com.emersondias.ebd.dtos.*;
import br.com.emersondias.ebd.dtos.location.CityDTO;
import br.com.emersondias.ebd.entities.SchoolProfile;
import br.com.emersondias.ebd.entities.enums.*;
import br.com.emersondias.ebd.services.interfaces.*;
import br.com.emersondias.ebd.utils.RandomUtils;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static br.com.emersondias.ebd.entities.enums.Gender.FEMALE;
import static br.com.emersondias.ebd.entities.enums.Gender.MALE;
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
    private final ILessonService lessonService;
    private final ISchoolProfileService schoolProfileService;
    private final Faker faker = new Faker();

    private static String generateEmailByName(String name) {
        var nameParts = removeAccents(name).toLowerCase().split(" ");
        var email = nameParts[0] + "." + nameParts[nameParts.length - 1] + "." + UUID.randomUUID() + "@" + chooseRandomElement("gmail.com", "hotmail.com", "yahoo.com.br", "msn.com");
        return email;
    }

    public void initializeTestData() {

        seedSchoolProfiles();

        var ageRanges = ageRangeService.findAll();
        var students = seedStudents(ageRanges);
        var teachers = seedTeachers();
        var classrooms = seedClassrooms(ageRanges, teachers, students);
        var lessons = seedLessons(classrooms);

        var teacherUsers = seedUsersByPeeople(teachers);
        List<UserDTO> users = seedUsers();

    }

    private List<SchoolProfileDTO> seedSchoolProfiles() {
        var address = AddressDTO.builder()
                .street("Rua Vereador Antônio Giacomassi")
                .number("25")
                .complement(null)
                .neighborhood("Alto Boqueirão")
                .zipCode("81770240")
                .city(CityDTO.builder().id(4106902L).build())
                .active(true)
                .build();
        var profile = SchoolProfileDTO.builder()
                .name("Assembleia de Deus")
                .subtitle("Congregação JD Castelo Branco")
                .address(address)
                .active(true)
                .build();
        return List.of(schoolProfileService.create(profile));
    }

    private List<LessonDTO> seedLessons(List<ClassroomDTO> classrooms) {
        List<LessonDTO> lessons = new ArrayList<>();
        final var START_DATE = LocalDate.now().minusDays(18);
        final var MAX_LESSONS = 20;
        final var LESSON_INTERVAL = 3;
        for (ClassroomDTO classroom : classrooms) {
            for (int i = 1; i <= MAX_LESSONS; i++) {
                var date = START_DATE.plusDays(i * LESSON_INTERVAL);
                var lesson = LessonDTO.builder()
                        .number(i)
                        .topic("Lição sobre " + faker.book().title())
                        .date(date)
                        .classroomId(classroom.getId())
                        .classroomName(classroom.getName())
                        .teachings(Set.of(teacherToTeaching(chooseRandomElement(classroom.getTeachers()))))
                        .active(true)
                        .build();
                final var NOW = LocalDate.now();
                if (date.isBefore(NOW)) {
                    lesson.setStatus(LessonStatus.FINALIZED);
                } else if (date.isEqual(NOW)) {
                    lesson.setStatus(RandomUtils.chooseRandomElement(LessonStatus.OPEN_SAME_DAY, LessonStatus.FINALIZED));
                } else {
                    lesson.setStatus(LessonStatus.OPEN_SAME_DAY);
                }
                if (LessonStatus.FINALIZED.equals(lesson.getStatus())) {
                    var attendances = classroom.getStudents()
                            .stream()
                            .map(student -> AttendanceDTO.builder()
                                    .present(RandomUtils.generateRandomBoolean())
                                    .studentId(student.getId())
                                    .studentName(student.getPerson().getName())
                                    .active(true)
                                    .build()
                            )
                            .collect(Collectors.toSet());
                    lesson.setAttendances(attendances);
                }
                lessons.add(lessonService.create(lesson));
            }
        }
        return lessons;
    }

    private TeachingDTO teacherToTeaching(TeacherDTO teacher) {
        return TeachingDTO.builder()
                .teacherId(teacher.getId())
                .active(true)
                .build();
    }

    private List<ClassroomDTO> seedClassrooms(List<AgeRangeDTO> ageRanges, List<PersonDTO> teachers, List<PersonDTO> students) {
        final int NUM_TEACHERS = 3;
        final int MAX_STUDENTS = 12;
        final var teachersList = new ArrayList<>(teachers);
        final var studentsList = new ArrayList<>(students);

        return ageRanges.stream()
                .map(ageRange -> {
                    Collections.shuffle(teachersList);
                    Collections.shuffle(studentsList);

                    var teachersGroup = teachersList.stream().limit(NUM_TEACHERS).toList();

                    var studentsGroup = students.stream()
                            .filter(student -> nonNull(student.getBirthdate()))
                            .filter(filterByAgeRange(ageRange))
                            .map(this::convertPersonToStudent)
                            .limit(MAX_STUDENTS)
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
        List<PersonDTO> teachers = new ArrayList<>();

        final int NUM_TEACHERS = 15;
        final int MIN_YEAR = LocalDate.now().getYear() - 99;
        final int MAX_YEAR = LocalDate.now().getYear() - 18;

        for (int i = 0; i < NUM_TEACHERS; i++) {
            var gender = RandomUtils.chooseRandomElement(MALE, FEMALE);
            var name = generateRandomFullName(gender);
            var email = generateEmailByName(name);
            var person = PersonDTO.builder()
                    .name(name)
                    .email(email)
                    .birthdate(generateRandomLocalDate(MIN_YEAR, MAX_YEAR))
                    .gender(gender)
                    .educationLevel(chooseRandomElement(EducationLevel.values()))
                    .maritalStatus(chooseRandomElement(MaritalStatus.values()))
                    .phoneNumbers(generateRandomPhoneNumbers(generateRandomInteger(0, 3)))
                    .types(Set.of(PersonType.TEACHER))
                    .address(generateRandomAddress())
                    .active(true)
                    .build();
            person = personService.create(person);
            teachers.add(person);
        }
        return teachers;
    }

    private List<PersonDTO> seedStudents(List<AgeRangeDTO> ageRanges) {
        List<PersonDTO> students = new ArrayList<>();

        for (AgeRangeDTO ageRange : ageRanges) {
            final int NUM_STUDENTS = RandomUtils.generateRandomInteger(1, 8);
            final int MIN_YEAR = LocalDate.now().getYear() - (nonNull(ageRange.getMaxAge()) ? ageRange.getMaxAge() : 80);
            final int MAX_YEAR = LocalDate.now().getYear() - (nonNull(ageRange.getMinAge()) ? ageRange.getMinAge() : 0);

            for (int i = 0; i < NUM_STUDENTS; i++) {
                var gender = RandomUtils.chooseRandomElement(MALE, FEMALE);
                var name = generateRandomFullName(gender);
                var email = generateEmailByName(name);
                var person = PersonDTO.builder()
                        .name(name)
                        .email(email)
                        .birthdate(generateRandomLocalDate(MIN_YEAR, MAX_YEAR))
                        .gender(gender)
                        .educationLevel(chooseRandomElement(EducationLevel.values()))
                        .maritalStatus(chooseRandomElement(MaritalStatus.values()))
                        .phoneNumbers(generateRandomPhoneNumbers(generateRandomInteger(0, 3)))
                        .types(Set.of(PersonType.STUDENT))
                        .address(generateRandomAddress())
                        .active(true)
                        .build();
                person = personService.create(person);
                students.add(person);
            }
        }
        return students;
    }

    private List<UserDTO> seedUsers() {
        List<UserDTO> users = new ArrayList<>();

        users.add(userService.create(UserDTO.builder().name("Emerson Dias").email("emersondias@hotmail.com")
                .password("Test@123").roles(Set.of(UserRole.ADMIN, UserRole.TEACHER)).active(true).build()));
        users.add(userService.create(UserDTO.builder().name("Testing").email("test@test.com").password("Test@123")
                .roles(Set.of(UserRole.TEACHER)).active(true).build()));

        return users;
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

    private String generateRandomFullName(Gender gender) {
        List<String> firstNames = new ArrayList<>(MALE.equals(gender) ? getMaleNames() : getFemaleNames());
        List<String> lastNames = new ArrayList<>(getLastNames());

        Collections.shuffle(firstNames);
        Collections.shuffle(lastNames);

        int numFirstNames = RandomUtils.generateRandomInteger(1, 2);
        int numLastNames = RandomUtils.generateRandomInteger(1, 2);

        List<String> fullNameParts = new ArrayList<>();
        fullNameParts.addAll(firstNames.subList(0, numFirstNames));
        fullNameParts.addAll(lastNames.subList(0, numLastNames));

        return String.join(" ", fullNameParts);
    }

    private List<String> getMaleNames() {
        return Arrays.asList(
                "João", "Pedro", "Lucas", "Gabriel", "Matheus",
                "Guilherme", "Rafael", "Felipe", "Bruno", "Daniel",
                "Thiago", "Vinícius", "André", "Rodrigo", "Eduardo",
                "Leonardo", "Carlos", "Diego", "Fernando", "Marcelo",
                "Igor", "Henrique", "Renato", "Luiz", "José",
                "Antônio", "Francisco", "Alexandre", "Ricardo", "Caio",
                "Murilo", "Vitor", "Samuel", "Paulo", "Alan",
                "Wesley", "Fábio", "Jorge", "Nathan", "Otávio",
                "Cláudio", "César", "Anderson", "Cristiano", "Leandro",
                "Everton", "Rogério", "Márcio", "Hugo", "Ivan",
                "Luciano", "Nelson", "Afonso", "Douglas", "Wallace",
                "Raul", "Jean", "Gilberto", "Érico", "Saulo",
                "Maurício", "Elias", "Danilo", "Sérgio", "Luan",
                "Adriano", "Alex", "Moacir", "Davi", "Jonathan",
                "Valter", "Matias", "Alisson", "Emanuel", "Artur",
                "Heitor", "Otto", "Joaquim", "Vicente", "Benjamin",
                "Noah", "Miguel", "Enzo", "Henrique", "Isaac",
                "Raí", "Geovani", "Jonas", "Jeferson", "Edson",
                "Alan", "Tales", "Ruan", "Washington", "Cléber",
                "Gilmar", "Tadeu", "Eliseu", "Silas", "Teodoro"
        );
    }


    private List<String> getFemaleNames() {
        return Arrays.asList(
                "Ana", "Maria", "Júlia", "Mariana", "Larissa",
                "Beatriz", "Camila", "Letícia", "Amanda", "Bruna",
                "Isabela", "Fernanda", "Rafaela", "Gabriela", "Patrícia",
                "Renata", "Natália", "Tatiane", "Vanessa", "Juliana",
                "Cristiane", "Débora", "Lívia", "Helena", "Carolina",
                "Elaine", "Michele", "Cláudia", "Cíntia", "Daniela",
                "Tatiana", "Simone", "Flávia", "Aline", "Priscila",
                "Adriana", "Verônica", "Kelly", "Valéria", "Andressa",
                "Carla", "Érica", "Viviane", "Regina", "Ângela",
                "Bianca", "Nicole", "Luana", "Taís", "Tainá",
                "Raquel", "Milena", "Cristina", "Luciana", "Suzana",
                "Silvana", "Eliane", "Neide", "Irene", "Paula",
                "Rebeca", "Joana", "Isadora", "Lorena", "Mônica",
                "Rosana", "Sueli", "Nádia", "Clarice", "Jéssica",
                "Graziella", "Maíra", "Lúcia", "Noemi", "Rosa",
                "Catarina", "Emanuele", "Mirela", "Celina", "Daniele",
                "Geovana", "Nathalia", "Tereza", "Ivone", "Aparecida",
                "Iara", "Rosângela", "Marisa", "Cléa", "Alessandra",
                "Melissa", "Giovana", "Débora", "Sandra", "Elaiza",
                "Fabiana", "Carine", "Dalila", "Tatieli", "Selma"
        );
    }


    private List<String> getLastNames() {
        return Arrays.asList(
                "Silva", "Santos", "Oliveira", "Souza", "Rodrigues",
                "Ferreira", "Alves", "Pereira", "Lima", "Gomes",
                "Costa", "Ribeiro", "Martins", "Carvalho", "Almeida",
                "Lopes", "Soares", "Fernandes", "Vieira", "Barbosa",
                "Rocha", "Dias", "Nunes", "Moreira", "Cardoso",
                "Teixeira", "Cavalcante", "Araújo", "Machado", "Monteiro",
                "Moura", "Batista", "Freitas", "Ramos", "Gonçalves",
                "Azevedo", "Medeiros", "Nascimento", "Andrade", "Rezende",
                "Correia", "Farias", "Tavares", "Leite", "Pinto",
                "Castro", "Campos", "Camargo", "Bezerra", "Sales",
                "Borges", "Antunes", "Miranda", "Barros", "Assis",
                "Marques", "Neves", "Queiroz", "Chaves", "Peixoto",
                "Brandão", "Figueiredo", "Coelho", "Morais", "Xavier",
                "Aguiar", "Moraes", "Cunha", "Brito", "Siqueira",
                "Fonseca", "Braga", "Valente", "Pinheiro", "Abreu",
                "Lacerda", "Viana", "Pacheco", "Carmo", "Serra",
                "Amaral", "Teles", "Prado", "Domingues", "Castilho",
                "Quevedo", "Simões", "Torres", "Rezende", "Galvão",
                "Garcia", "Nascimento", "Furtado", "Matos", "Porto",
                "Beltrão", "Lourenço", "Henrique", "Guimarães", "Saraiva",
                "Severino", "Lemos", "Barreto", "Sampaio", "Bittencourt"
        );
    }
}
