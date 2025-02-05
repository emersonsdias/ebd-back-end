package br.com.emersondias.ebd.controllers;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.location.CitySimpleDTO;
import br.com.emersondias.ebd.dtos.location.StateDTO;
import br.com.emersondias.ebd.services.interfaces.ILocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Location", description = "the location API")
@RestController
@RequestMapping(value = RouteConstants.LOCATIONS_ROUTE)
@RequiredArgsConstructor
public class LocationController {

    private final ILocationService locationService;

    @Operation(summary = "Sync states and cities from IBGE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204")
    })
    @GetMapping(value = "/sync")
    public ResponseEntity<Void> sync() {
        locationService.syncStatesFromIBGE();
        locationService.syncCitiesFromIBGE();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find all active states")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/states")
    public ResponseEntity<List<StateDTO>> findAllStates() {
        return ResponseEntity.ok(locationService.findAllStates());
    }

    @Operation(summary = "Find all active cities by state id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/states/{stateId}/cities")
    public ResponseEntity<List<CitySimpleDTO>> findCitiesByStateId(@PathVariable Long stateId) {
        return ResponseEntity.ok(locationService.findCitiesByStateId(stateId));
    }
}
