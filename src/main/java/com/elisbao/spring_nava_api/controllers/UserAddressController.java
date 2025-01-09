package com.elisbao.spring_nava_api.controllers;

import com.elisbao.spring_nava_api.annotations.LogOperation;
import com.elisbao.spring_nava_api.models.UserAddress;
import com.elisbao.spring_nava_api.models.dto.UserAddressDTO;
import com.elisbao.spring_nava_api.services.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/address")
@Tag(name = "Addresses Management", description = "Operations related to Addresses Management")
@Validated
public class UserAddressController {

    private final UserAddressService userAddressService;

    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @Operation(
            summary = "Find all addresses by user",
            description = "Retrieve all addresses associated with the current user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Addresses found", content = @Content(schema = @Schema(implementation = UserAddressDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Addresses not found")
            }
    )
    @GetMapping("/user")
    public ResponseEntity<List<UserAddress>> findAllByUser() {
        List<UserAddress> objs = this.userAddressService.findAllByUser();
        return ResponseEntity.ok().body(objs);
    }

    @Operation(
            summary = "Create a new address",
            description = "Create a new address for the current user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Address created", content = @Content(schema = @Schema(implementation = UserAddress.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    @LogOperation
    public ResponseEntity<Void> create(
            @Parameter(description = "Address object to be created") @Valid @RequestBody UserAddressDTO obj) {
        UserAddress userAddress =  this.userAddressService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(userAddress.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(
            summary = "Update an existing address",
            description = "Update the details of an existing address by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Address updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Address not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Parameter(description = "Updated address object") @Valid @RequestBody UserAddressDTO obj,
            @Parameter(description = "ID of the address to be updated") @PathVariable Long id) {
        this.userAddressService.update(obj,id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete a address",
            description = "Delete a address by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Address not found")
            }
    )
    @DeleteMapping("/{id}")
    @LogOperation
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the address to be deleted") @PathVariable Long id) {
        this.userAddressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
