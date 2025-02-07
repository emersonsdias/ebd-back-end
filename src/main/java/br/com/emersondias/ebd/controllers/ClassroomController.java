package br.com.emersondias.ebd.controllers;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.ClassroomDTO;
import br.com.emersondias.ebd.services.interfaces.IClassroomService;
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

@Tag(name = "Classroom", description = "the classroom API")
@RestController
@RequestMapping(value = RouteConstants.CLASSROOMS_ROUTE)
@RequiredArgsConstructor
public class ClassroomController {

    private final IClassroomService classroomService;

    @Operation(summary = "Create a new classroom")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")
    })
    @PostMapping
    public ResponseEntity<ClassroomDTO> create(@RequestBody @Valid ClassroomDTO classroomDTO) {
        classroomDTO = classroomService.create(classroomDTO);
        return ResponseEntity.created(URIUtils.buildUri(classroomDTO.getId())).body(classroomDTO);
    }

    @Operation(summary = "Update existing classroom")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClassroomDTO> update(@RequestBody @Valid ClassroomDTO classroomDTO) {
        classroomDTO = classroomService.update(classroomDTO);
        return ResponseEntity.ok(classroomDTO);
    }

    @Operation(summary = "Delete a classroom by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        classroomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find classroom by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClassroomDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.findById(id));
    }

    @Operation(summary = "Find all classrooms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping
    public ResponseEntity<List<ClassroomDTO>> findAll() {
        return ResponseEntity.ok(classroomService.findAll());
    }

}
