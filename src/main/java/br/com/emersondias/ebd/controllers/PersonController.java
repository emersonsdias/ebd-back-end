package br.com.emersondias.ebd.controllers;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.PersonDTO;
import br.com.emersondias.ebd.dtos.PersonReportDTO;
import br.com.emersondias.ebd.entities.enums.PersonType;
import br.com.emersondias.ebd.services.interfaces.IPersonService;
import br.com.emersondias.ebd.utils.URIUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Person", description = "the person API")
@RestController
@RequestMapping(value = RouteConstants.PEOPLE_ROUTE)
@RequiredArgsConstructor
public class PersonController {

    private final IPersonService personService;

    @Operation(summary = "Create a new person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")
    })
    @PostMapping
    public ResponseEntity<PersonDTO> create(@RequestBody @Valid PersonDTO personDTO) {
        personDTO = personService.create(personDTO);
        return ResponseEntity.created(URIUtils.buildUri(personDTO.getId())).body(personDTO);
    }

    @Operation(summary = "Update existing person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<PersonDTO> update(@RequestBody @Valid PersonDTO personDTO) {
        personDTO = personService.update(personDTO);
        return ResponseEntity.ok(personDTO);
    }

    @Operation(summary = "Delete a person by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find person by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @Operation(summary = "Find all persons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping
    public ResponseEntity<List<PersonDTO>> findAll() {
        return ResponseEntity.ok(personService.findAll());
    }

    @Operation(summary = "Find persons by types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping("/by-type")
    public ResponseEntity<List<PersonDTO>> findByTypes(
            @RequestParam(value = "personTypes") List<PersonType> types
    ) {
        return ResponseEntity.ok(personService.findByPersonType(types));
    }

    @Operation(summary = "Generate person report")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping(value = "/{id}/report")
    public ResponseEntity<PersonReportDTO> generatePersonReport(@PathVariable UUID id) {
        PersonReportDTO report = personService.generatePersonReport(id);
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Generate person report pdf")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary")))
    })
    @GetMapping(value = "/{id}/report/pdf")
    public ResponseEntity<byte[]> generatePersonPdf(@PathVariable UUID id) {
        byte[] pdf = personService.generatePersonReportPdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "pessoa_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @Operation(summary = "Find all inactive people")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/inactive")
    public ResponseEntity<List<PersonDTO>> findInactivePeople() {
        return ResponseEntity.ok(personService.findAllInactive());
    }

    @Operation(summary = "Find all people without user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/without-user")
    public ResponseEntity<List<PersonDTO>> findPeopleWithoutUser() {
        return ResponseEntity.ok(personService.findAllWithoutUser());
    }

    @Operation(summary = "Find people by birthdate period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/birthdate")
    public ResponseEntity<List<PersonDTO>> findByBirthdatePeriod(
            @RequestParam(value = "startDate") LocalDate startDate,
            @RequestParam(value = "endDate") LocalDate endDate
    ) {
        return ResponseEntity.ok(personService.findByBirthdatePeriod(startDate, endDate));
    }


}
