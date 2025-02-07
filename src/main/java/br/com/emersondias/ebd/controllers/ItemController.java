package br.com.emersondias.ebd.controllers;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.dtos.ItemDTO;
import br.com.emersondias.ebd.services.interfaces.IItemService;
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

@Tag(name = "Item", description = "the item API")
@RestController
@RequestMapping(value = RouteConstants.ITEMS_ROUTE)
@RequiredArgsConstructor
public class ItemController {

    private final IItemService itemService;

    @Operation(summary = "Create a new item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")
    })
    @PostMapping
    public ResponseEntity<ItemDTO> create(@RequestBody @Valid ItemDTO itemDTO) {
        itemDTO = itemService.create(itemDTO);
        return ResponseEntity.created(URIUtils.buildUri(itemDTO.getId())).body(itemDTO);
    }

    @Operation(summary = "Update existing item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<ItemDTO> update(@RequestBody @Valid ItemDTO itemDTO) {
        itemDTO = itemService.update(itemDTO);
        return ResponseEntity.ok(itemDTO);
    }

    @Operation(summary = "Delete a item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ItemDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    @Operation(summary = "Find all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping
    public ResponseEntity<List<ItemDTO>> findAll() {
        return ResponseEntity.ok(itemService.findAll());
    }
}
