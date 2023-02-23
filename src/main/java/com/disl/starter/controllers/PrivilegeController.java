package com.disl.starter.controllers;

import com.disl.starter.config.CommonApiResponses;
import com.disl.starter.entities.Privilege;
import com.disl.starter.models.Response;
import com.disl.starter.services.PrivilegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
@CommonApiResponses
@RequestMapping("/api/privilege")
public class PrivilegeController {

	@Autowired
	private PrivilegeService privilegeService;

    @Operation(summary = "Get all privilege list", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Privilege.class))), responseCode = "200")
	@GetMapping
	public ResponseEntity<Response> getAllPrivileges() {
		return Response.getResponseEntity(
				true, "Data loaded successfully.",
				privilegeService.viewAllPrivileges()
		);
	}
}
