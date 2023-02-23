package com.disl.starter.config;

import com.disl.starter.models.Response;
import com.disl.starter.models.responses.DefaultErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "create product", security = @SecurityRequirement(name = "jwtToken"))
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = @Content(schema = @Schema(implementation = Response.class))
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(schema = @Schema(implementation = Response.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(schema = @Schema(implementation = DefaultErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Resource Not Found",
                content = @Content(schema = @Schema(implementation = DefaultErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Server Failure",
                content = @Content(schema = @Schema(implementation = DefaultErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "502",
                description = "Internal Server Error",
                content = @Content(schema = @Schema(implementation = DefaultErrorResponse.class))
        ),
})
public @interface CommonApiResponses {}
