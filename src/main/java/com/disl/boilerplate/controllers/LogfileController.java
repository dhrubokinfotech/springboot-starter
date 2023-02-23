package com.disl.boilerplate.controllers;

import ch.qos.logback.classic.LoggerContext;
import com.disl.boilerplate.models.Response;
import com.disl.boilerplate.models.responses.ApplicationLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.disl.boilerplate.BoilerplateApplication.logger;

@RestController
@RequestMapping("/api/log-file")
public class LogfileController {

    @Operation(summary = "Get all log files from the file system", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = ApplicationLog.class))), responseCode = "200")
    @PreAuthorize("hasAuthority('LOG_READ')")
    @GetMapping("/logfiles")
    public ResponseEntity<Response> getLog(
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "keyword", defaultValue = "") String keyword
    ) {
        List<File> files = new ArrayList<>();
        this.generateFileList(getLogFilePath(), files);
        List<ApplicationLog> prWireLogList = new ArrayList<>();

        if (keyword != null && keyword.length() > 0) {
            for (File file : files) {
                if (file.getName().contains(keyword) && !file.isDirectory()) {
                    ApplicationLog prWireLog = new ApplicationLog();
                    prWireLog.setFileName(file.getName());
                    Date date = new Date(file.lastModified());
                    prWireLog.setLastModified(date);
                    prWireLog.setFileSize(getReadableFileSize(file.length()));
                    prWireLogList.add(prWireLog);
                }
            }
        } else {
            for (File file : files) {
                ApplicationLog prWireLog = new ApplicationLog();
                prWireLog.setFileName(file.getName());
                Date date = new Date(file.lastModified());
                prWireLog.setLastModified(date);
                prWireLog.setFileSize(getReadableFileSize(file.length()));
                prWireLogList.add(prWireLog);
            }
        }
        prWireLogList.sort((log1, log2) -> log2.getLastModified().compareTo(log1.getLastModified()));

        int logSize = prWireLogList.size();
        int totalPages = logSize / pageSize;
        PageRequest pageable = PageRequest.of(pageNo, pageSize);

        int max = pageNo >= totalPages ? logSize : (pageSize * (pageNo + 1));
        int min = pageNo > totalPages ? max : (pageSize * pageNo);

        return Response.getResponseEntity(true, "Log files loaded",
                new PageImpl<>(prWireLogList.subList(min, max), pageable, logSize)
        );
    }

    @Operation(summary = "Download a log file like fileName=logfile-2021-04-22.0.log", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseEntity.class)), responseCode = "200")
    @PreAuthorize("hasAuthority('LOG_WRITE')")
    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam(name = "fileName") String fileName) {
        try {
            List<File> files = new ArrayList<>();
            this.generateFileList(getLogFilePath(), files);
            String absolutePath = "";

            for (File file : files) {
                if (file.getName().contentEquals(fileName)) {
                    absolutePath = file.getAbsolutePath();
                    break;
                }
            }

            if (absolutePath.isEmpty()) return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST,
                    false, "No file found with this file name", null), HttpStatus.OK);

            byte[] array = Files.readAllBytes(Paths.get(absolutePath));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/plain"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(array));

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST, false,
                    "Something went wrong: " + e.getMessage(), null), HttpStatus.OK);
        }
    }

    @Operation(summary = "Delete a log file except main file(logfile.log)", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)), responseCode = "200")
    @PreAuthorize("hasAuthority('LOG_WRITE')")
    @DeleteMapping("/delete")
    public Response deleteFile(@RequestParam(name = "fileName") String fileName, @RequestParam(name = "userAuthId") long userAuthId) {
        Response response = new Response(HttpStatus.BAD_REQUEST, false, "file not deleted", false);
        List<File> files = new ArrayList<>();
        this.generateFileList(getLogFilePath(), files);

        String absolutePath = "";
        for (File file : files) {
            if (file.getName().contentEquals(fileName)) {
                absolutePath = file.getAbsolutePath();
                break;
            }
        }

        if (absolutePath.isEmpty()) {
            response.setMessage("No file found with this file name");
            return response;
        }

        File file = new File(absolutePath);
        if (file.getName().equals("logfile.log")) {
            response.setMessage("You can not delete the main logfile. Try to delete older log file");
            return response;
        }

        if (FileSystemUtils.deleteRecursively(file.getParentFile())) {
            response.setSuccess(true);
            response.setPayload(true);
            response.setStatus(HttpStatus.OK);
            response.setMessage("log file deleted");
        }

        return response;
    }

    private void generateFileList(String directoryName, List<File> files) {
        File directory = new File(directoryName);
        File[] fileList = directory.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) generateFileList(file.getAbsolutePath(), files);
                else if (file.isFile()) files.add(file);
            }
        }
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[] {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups)) + " " + units[digitGroups];
    }

    private String getLogFilePath() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        String logDirPath = loggerContext.getProperty("LOG_DIRECTORY");

        logger.info("Logfile path => {}", logDirPath);
        return logDirPath;
    }
}
