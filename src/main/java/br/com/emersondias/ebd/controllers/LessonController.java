package br.com.emersondias.ebd.controllers;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.LessonDTO;
import br.com.emersondias.ebd.dtos.filters.LessonFilterDTO;
import br.com.emersondias.ebd.entities.enums.LessonStatus;
import br.com.emersondias.ebd.services.interfaces.ILessonService;
import br.com.emersondias.ebd.utils.URIUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Lesson", description = "the lesson API")
@RestController
@RequestMapping(value = RouteConstants.LESSONS_ROUTE)
@RequiredArgsConstructor
public class LessonController {

    private final ILessonService lessonService;

    @Operation(summary = "Create a new lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")
    })
    @PostMapping
    public ResponseEntity<LessonDTO> create(@RequestBody @Valid LessonDTO lessonDTO) {
        lessonDTO = lessonService.create(lessonDTO);
        return ResponseEntity.created(URIUtils.buildUri(lessonDTO.getId())).body(lessonDTO);
    }

    @Operation(summary = "Update existing lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<LessonDTO> update(@RequestBody @Valid LessonDTO lessonDTO) {
        lessonDTO = lessonService.update(lessonDTO);
        return ResponseEntity.ok(lessonDTO);
    }

    @Operation(summary = "Delete a lesson by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find lesson by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<LessonDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.findById(id));
    }

    @Operation(summary = "Find all lessons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping
    public ResponseEntity<List<LessonDTO>> findAll(
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "maxRecentLessons", required = false) Integer maxRecentLessons,
            @RequestParam(value = "lessonNumber", required = false) Integer lessonNumber,
            @RequestParam(value = "lessonStatus", required = false) LessonStatus lessonStatus,
            HttpServletRequest request
    ) {
        if (request.getParameterMap().isEmpty()) {
            return ResponseEntity.ok(lessonService.findAll());
        }

        var filter = LessonFilterDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .lessonNumber(lessonNumber)
                .lessonStatus(lessonStatus)
                .build();

        return ResponseEntity.ok(lessonService.findByFilter(filter));
    }

    @Operation(summary = "Find lessons by list of IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PostMapping("/batch")
    public ResponseEntity<List<LessonDTO>> findByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(lessonService.findByIds(ids));
    }

    @Operation(summary = "Generate lesson report pdf")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary")))
    })
    @GetMapping(value = "/report/pdf")
    public ResponseEntity<byte[]> generateLessonPdf(@RequestParam(value = "lessonNumber") Integer lessonNumber,
                                                    @RequestParam(value = "startDate") LocalDate startDate,
                                                    @RequestParam(value = "endDate") LocalDate endDate
        ) {
        byte[] pdf = lessonService.generateLessonUnitReportPdf(lessonNumber, startDate, endDate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "lesson_" + lessonNumber + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
