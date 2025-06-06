package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.*;
import br.com.emersondias.ebd.entities.Address;
import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.entities.enums.PersonType;
import br.com.emersondias.ebd.exceptions.ReportGenerationException;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.CityMapper;
import br.com.emersondias.ebd.mappers.PersonMapper;
import br.com.emersondias.ebd.mappers.PhoneNumberMapper;
import br.com.emersondias.ebd.repositories.PersonRepository;
import br.com.emersondias.ebd.repositories.specifications.PersonSpecification;
import br.com.emersondias.ebd.services.interfaces.*;
import br.com.emersondias.ebd.utils.LogHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements IPersonService {

    private static final LogHelper LOG = LogHelper.getInstance();

    private final PersonRepository repository;
    private final IClassroomService classroomService;
    private final IAttendanceService attendanceService;
    private final ITeachingService teachingService;
    private final IReportService reportService;

    @Transactional
    @Override
    public PersonDTO create(PersonDTO personDTO) {
        requireNonNull(personDTO);
        personDTO.setId(null);
        var personEntity = PersonMapper.toEntity(personDTO);
        personEntity = repository.save(personEntity);
        return PersonMapper.toDTO(personEntity);
    }

    @Transactional
    @Override
    public PersonDTO update(PersonDTO personDTO) {
        requireNonNull(personDTO);
        requireNonNull(personDTO.getId());
        var personEntity = findEntityById(personDTO.getId());
        updateData(personEntity, personDTO);
        personEntity = repository.save(personEntity);
        return PersonMapper.toDTO(personEntity);
    }

    private void updateData(Person personEntity, PersonDTO personDTO) {
        personEntity.setName(personDTO.getName());
        personEntity.setBirthdate(personDTO.getBirthdate());
        personEntity.setEmail(personDTO.getEmail());
        personEntity.setGender(personDTO.getGender());
        personEntity.setEducationLevel(personDTO.getEducationLevel());
        personEntity.setMaritalStatus(personDTO.getMaritalStatus());
        personEntity.setTypes(personDTO.getTypes());
        personEntity.setActive(personDTO.isActive());
        personEntity.setPhoneNumbers(personDTO.getPhoneNumbers().stream().map(PhoneNumberMapper::toEntity).toList());
        updateAddress(personEntity.getAddress(), personDTO.getAddress());
    }

    private void updateAddress(Address addressEntity, AddressDTO addressDTO) {
        addressEntity.setStreet(addressDTO.getStreet());
        addressEntity.setNumber(addressDTO.getNumber());
        addressEntity.setComplement(addressDTO.getComplement());
        addressEntity.setNeighborhood(addressDTO.getNeighborhood());
        addressEntity.setZipCode(addressDTO.getZipCode());
        addressEntity.setCity(CityMapper.toEntity(addressDTO.getCity()));
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDTO findById(UUID id) {
        requireNonNull(id);
        return PersonMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<PersonDTO> findAll() {
        return repository.findAll().stream().map(PersonMapper::toDTO).toList();
    }

    @Override
    public PersonReportDTO generatePersonReport(UUID id) {
        requireNonNull(id);
        var personDTO = findById(id);
        var studentClassrooms = classroomService.findByStudentsPersonId(id);
        var attendances = attendanceService.findByStudentPersonId(id);

        List<ClassroomAttendanceDTO> attendancesByClassroom = new ArrayList<>();
        studentClassrooms.forEach(classroom -> {
            var attendacesFiltered = attendances.stream()
                    .filter(a -> a.getLesson().getClassroomId().equals(classroom.getId())).toList();
            attendancesByClassroom.add(new ClassroomAttendanceDTO(classroom, attendacesFiltered));
        });

        var teacherClassrooms = classroomService.findByTeachersPersonId(id);
        var teachings = teachingService.findByTeacherPersonId(id);

        List<ClassroomTeacherDTO> teachingsByClassroom = new ArrayList<>();
        teacherClassrooms.forEach(classroom -> {
            var teachingsFiltered = teachings.stream()
                    .filter(t -> t.getLesson().getClassroomId().equals(classroom.getId())).toList();
            teachingsByClassroom.add(new ClassroomTeacherDTO(classroom, teachingsFiltered));
        });

        return new PersonReportDTO(personDTO, attendancesByClassroom, teachingsByClassroom);
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] generatePersonReportPdf(UUID id) {
        requireNonNull(id);
        var personReport = generatePersonReport(id);

        Map<String, Object> params = new HashMap<>();
        params.put("person_id", id.toString());
        params.put("qr_code_value",
                "http://localhost:8080"
                        .concat(RouteConstants.PEOPLE_ROUTE)
                        .concat("/")
                        .concat(personReport.getPerson().getId().toString())
        );
        try {
            return reportService.generatePdf("person_report/person_report", params);
        } catch (ReportGenerationException e) {
            var error = "Failed to build person pdf, person id: '" + id + "'";
            LOG.error(error);
            throw new RuntimeException(error, e);
        }
    }

    @Override
    public List<PersonDTO> findByPersonType(List<PersonType> types) {
        requireNonNull(types);
        return repository.findByTypesInOrderByName(types).stream().map(PersonMapper::toDTO).toList();
    }

    @Override
    public List<PersonDTO> findAllInactive() {
        return repository.findInactive().stream().map(PersonMapper::toDTO).toList();
    }

    @Override
    public List<PersonDTO> findAllWithoutUser() {
        return repository.findWithoutUser().stream().map(PersonMapper::toDTO).toList();
    }

    @Override
    public List<PersonDTO> findByBirthdatePeriod(LocalDate startDate, LocalDate endDate) {
        requireNonNull(startDate);
        requireNonNull(endDate);

        int startDateInt = startDate.getMonth().getValue() * 100 + startDate.getDayOfMonth();
        int endDateInt = endDate.getMonth().getValue() * 100 + endDate.getDayOfMonth();

        if (startDateInt < endDateInt) {
            var specification = PersonSpecification.birthdateBetween(startDate, endDate);
            return repository.findAll(specification).stream().map(PersonMapper::toDTO).toList();
        }
        var lastDayYear = LocalDate.of(startDate.getYear(), Month.DECEMBER, 31);
        var firstDayYear = lastDayYear.plusDays(1);

        List<Person> people = new ArrayList<>();
        people.addAll(repository.findByBirthdateCrossingYear(startDate, lastDayYear));
        people.addAll(repository.findByBirthdateCrossingYear(firstDayYear, endDate));

        return people.stream().map(PersonMapper::toDTO).toList();
    }

    private Person findEntityById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Person.class));
    }


}
