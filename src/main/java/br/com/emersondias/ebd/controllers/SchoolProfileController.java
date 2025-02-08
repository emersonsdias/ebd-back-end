package br.com.emersondias.ebd.controllers;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.SchoolProfileDTO;
import br.com.emersondias.ebd.services.interfaces.ISchoolProfileService;
import br.com.emersondias.ebd.utils.URIUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SchoolProfile", description = "the schoolProfile API")
@RestController
@RequestMapping(value = RouteConstants.SCHOOL_PROFILES_ROUTE)
@RequiredArgsConstructor
public class SchoolProfileController {

    private final ISchoolProfileService schoolProfileService;

    @Operation(summary = "Create a new school profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")
    })
    @PostMapping
    public ResponseEntity<SchoolProfileDTO> create(@RequestBody @Valid SchoolProfileDTO schoolProfileDTO) {
        schoolProfileDTO = schoolProfileService.create(schoolProfileDTO);
        return ResponseEntity.created(URIUtils.buildUri(schoolProfileDTO.getId())).body(schoolProfileDTO);
    }

    @Operation(summary = "Update existing school profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<SchoolProfileDTO> update(@RequestBody @Valid SchoolProfileDTO schoolProfileDTO) {
        schoolProfileDTO = schoolProfileService.update(schoolProfileDTO);
        return ResponseEntity.ok(schoolProfileDTO);
    }

    @Operation(summary = "Delete a school profile by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        schoolProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find school profile by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<SchoolProfileDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolProfileService.findById(id));
    }

    @Operation(summary = "Find all school profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping
    public ResponseEntity<List<SchoolProfileDTO>> findAll() {
        return ResponseEntity.ok(schoolProfileService.findAll());
    }
}
