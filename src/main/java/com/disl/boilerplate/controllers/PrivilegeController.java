package com.disl.boilerplate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.disl.boilerplate.models.Privilege;
import com.disl.boilerplate.payloads.Response;
import com.disl.boilerplate.services.PrivilegeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@CrossOrigin()
@RequestMapping("/privilege")
public class PrivilegeController {

	@Autowired
	private PrivilegeService privilegeService;

	@ApiOperation(value = "Get all privilege list",authorizations = {@Authorization(value = "jwtToken")})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = Privilege.class, responseContainer = "List"), 
	@ApiResponse(code = 401, message = "Unauthorized"),
	@ApiResponse(code = 403, message="Forbidden"),
	@ApiResponse(code = 404, message = "Not Found"),
	@ApiResponse(code = 500, message = "Failure")})
	@GetMapping
	public Response getAllPrivileges() {
		return new Response(HttpStatus.OK, true, "Data loaded successfully.", privilegeService.viewAllPrivileges());
	}
}
