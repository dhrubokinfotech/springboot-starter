package com.disl.boilerplate.config;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value =
        {
                @ApiResponse(code = 400, message = "Bad_Request"),
                @ApiResponse(code = 401, message = "Unauthorized"),
                @ApiResponse(code = 403, message = "Forbidden"),
                @ApiResponse(code = 404, message = "Not Found"),
                @ApiResponse(code = 500, message = "Server Failure")
        }
)
public @interface CommonApiResponses {
}