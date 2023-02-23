package com.disl.starter.features.db_file.controller;

import com.disl.starter.config.CommonApiResponses;
import com.disl.starter.features.db_file.enums.FileUploadType;
import com.disl.starter.features.db_file.entity.DbFile;
import com.disl.starter.features.db_file.service.DbFileService;
import com.disl.starter.models.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CommonApiResponses
@RequestMapping("/api/db-file")
public class DbFileController {

    @Autowired
    private DbFileService dbFileService;

    @Operation(summary = "upload file", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = DbFile.class)), responseCode = "200")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<Response> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileUploadType") FileUploadType fileUploadType,
            @RequestParam("fileType") String fileType
    ) {
        if (file.isEmpty()) {
            return Response.getResponseEntity(false, "File can't be null or empty", null);
        }

        try {
            DbFile savedFile = dbFileService.storeFile(file, file.getName(), fileType, fileUploadType);
            return Response.getResponseEntity(HttpStatus.OK, "File stored with success", savedFile);
        } catch (Exception e) {
            return Response.getResponseEntity(false, e.getMessage(), null);
        }
    }

    @Operation(summary = "get db-file by id")
    @ApiResponse(content = @Content(schema = @Schema(implementation = DbFile.class)), responseCode = "200")
    @GetMapping("/id/{id}")
    public ResponseEntity<Response> getFile(@PathVariable("id") long id) {
        DbFile file = dbFileService.findById(id);

        if (file == null) {
            return Response.getResponseEntity(false, "Invalid file id", null);
        } else {
            return Response.getResponseEntity(true, "File loaded", file);
        }
    }
}
