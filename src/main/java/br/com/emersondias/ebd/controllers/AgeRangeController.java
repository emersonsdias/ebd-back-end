package br.com.emersondias.ebd.controllers;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.AgeRangeDTO;
import br.com.emersondias.ebd.services.interfaces.IAgeRangeService;
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

@Tag(name = "AgeRange", description = "the ageRange API")
@RestController
@RequestMapping(value = RouteConstants.AGE_RANGES_ROUTE)
@RequiredArgsConstructor
public class AgeRangeController {

    private final IAgeRangeService ageRangeService;

    @Operation(summary = "Create a new age range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")
    })
    @PostMapping
    public ResponseEntity<AgeRangeDTO> create(@RequestBody @Valid AgeRangeDTO ageRangeDTO) {
        ageRangeDTO = ageRangeService.create(ageRangeDTO);
        return ResponseEntity.created(URIUtils.buildUri(ageRangeDTO.getId())).body(ageRangeDTO);
    }

    @Operation(summary = "Update existing age range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<AgeRangeDTO> update(@RequestBody @Valid AgeRangeDTO ageRangeDTO) {
        ageRangeDTO = ageRangeService.update(ageRangeDTO);
        return ResponseEntity.ok(ageRangeDTO);
    }

    @Operation(summary = "Delete a age range by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ageRangeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find age range by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<AgeRangeDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ageRangeService.findById(id));
    }

    @Operation(summary = "Find all age ranges")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping
    public ResponseEntity<List<AgeRangeDTO>> findAll() {
        return ResponseEntity.ok(ageRangeService.findAll());
    }
}
