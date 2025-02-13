package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.controllers.ClassroomAttendanceDTO;
import br.com.emersondias.ebd.dtos.AddressDTO;
import br.com.emersondias.ebd.dtos.PersonDTO;
import br.com.emersondias.ebd.dtos.PersonReportDTO;
import br.com.emersondias.ebd.entities.Address;
import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.mappers.CityMapper;
import br.com.emersondias.ebd.mappers.PersonMapper;
import br.com.emersondias.ebd.mappers.PhoneNumberMapper;
import br.com.emersondias.ebd.repositories.PersonRepository;
import br.com.emersondias.ebd.services.interfaces.IAttendanceService;
import br.com.emersondias.ebd.services.interfaces.IClassroomService;
import br.com.emersondias.ebd.services.interfaces.IPersonService;
import br.com.emersondias.ebd.services.interfaces.IReportService;
import br.com.emersondias.ebd.utils.LogHelper;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements IPersonService {

    private static final LogHelper LOG = LogHelper.getInstance();

    private final PersonRepository repository;
    private final IClassroomService classroomService;
    private final IAttendanceService attendanceService;
    private final IReportService reportService;

    @Transactional
    @Override
    public PersonDTO create(PersonDTO personDTO) {
        requireNonNull(personDTO);
        personDTO.setId(null);
        personDTO.setActive(true);
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

    @Override
    public void delete(UUID id) {
        requireNonNull(id);
        repository.deleteById(id);
    }

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
        var classrooms = classroomService.findByStudentsPersonId(id);
        var attendances = attendanceService.findByStudentPersonId(id);
        List<ClassroomAttendanceDTO> attendancesByClassroom = new ArrayList<>();

        classrooms.forEach(classroom -> {
            var attendacesFiltered = attendances.stream()
                    .filter(a -> a.getLesson().getClassroomId().equals(classroom.getId())).toList();
            attendancesByClassroom.add(new ClassroomAttendanceDTO(classroom, attendacesFiltered));
        });

        return new PersonReportDTO(personDTO, attendancesByClassroom);
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] generatePersonReportPdf(UUID id) {
        requireNonNull(id);
        var personReport = generatePersonReport(id);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(personReport));

        Map<String, Object> params = new HashMap<>();
        params.put("qrCodeValue",
                "http://localhost:8080/"
                        .concat(RouteConstants.PEOPLE_ROUTE)
                        .concat("/")
                        .concat(personReport.getPerson().getId().toString())
        );
        params.put("attendancesByClassroom", new JRBeanCollectionDataSource(personReport.getAttendancesByClassroom()));

        try {
            return reportService.generatePdf("person_report", params, dataSource);
        } catch (JRException | IOException e) {
            var error = "Failed to build person pdf, person id: '" + id + "'";
            LOG.error(error);
            throw new RuntimeException(error, e);
        }
    }

    private Person findEntityById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, Person.class));
    }


}
