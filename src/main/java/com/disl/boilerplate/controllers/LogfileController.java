package com.disl.boilerplate.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.disl.boilerplate.constants.AppConstants;
import com.disl.boilerplate.models.responses.ApplicationLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/logging")
public class LogfileController {

	@ApiOperation(value = "Get all log files from the file system", authorizations = {@Authorization(value = "jwtToken")})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = ApplicationLog.class),@ApiResponse(code = 401, message = "Unauthorized"),@ApiResponse(code = 403, message="Forbidden"),@ApiResponse(code = 404, message = "Not Found"),@ApiResponse(code = 500, message = "Failure")})
	@PreAuthorize("hasPermission(null, 'USER', 'READ')")
	@GetMapping("/logfiles")
    public Page<ApplicationLog> getLog(
    		@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "keyword", defaultValue = "") String keyword) throws Exception {

        File root = new File(AppConstants.logfilePath());
        System.out.println("Root file = " + root.getAbsolutePath());
        List<File> files = new ArrayList<>();
        this.listf(AppConstants.logfilePath(), files);

        System.out.println("Logfile path = " + AppConstants.logfilePath());
        System.out.println("#### Files ###");
        for (File f : files) {
            System.out.println(f.getName());
        }
        System.out.println("#### Files ###");

        List<ApplicationLog> applicationLogs = new ArrayList<ApplicationLog>();

        if (keyword != null && keyword.length() > 0) {
            for (File f : files) {
                if (f.getName().contains(keyword) && f.isDirectory() == false) {
                	ApplicationLog applicationLog = new ApplicationLog();
                	applicationLog.setFileName(f.getName());
                    Date d = new Date(f.lastModified());
                    applicationLog.setLastModified(d);
                    applicationLogs.add(applicationLog);
                }
            }
        } else {
            for (File f : files) {
            	ApplicationLog applicationLog = new ApplicationLog();
            	applicationLog.setFileName(f.getName());
                Date d = new Date(f.lastModified());
                applicationLog.setLastModified(d);
                applicationLogs.add(applicationLog);
            }
        }

        Collections.sort(applicationLogs, new Comparator<ApplicationLog>() {
            @Override
            public int compare(ApplicationLog o1, ApplicationLog o2) {
                return o2.getLastModified().compareTo(o1.getLastModified());
            }
        });

        PagedListHolder<ApplicationLog> pageHolder = new PagedListHolder<ApplicationLog>(applicationLogs);
        pageHolder.setPageSize(pageSize);
        pageHolder.setPage(pageNo);
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return new PageImpl<>(pageHolder.getPageList(), paging, applicationLogs.size());
    }

    public void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
        }
    }

    @ApiOperation(value = "Download a log file from the file system", authorizations = {@Authorization(value = "jwtToken")})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),@ApiResponse(code = 401, message = "Unauthorized"),@ApiResponse(code = 403, message="Forbidden"),@ApiResponse(code = 404, message = "Not Found"),@ApiResponse(code = 500, message = "Failure")})
    @PreAuthorize("hasPermission(null, 'USER', 'READ')")
    @GetMapping("/download-log")
    public ResponseEntity<?> downloadFile(@RequestParam(name = "fileName") String fileName) throws Exception {

        try {
        		File root = new File(AppConstants.logfilePath());
			System.out.println("Root file = " + root.getAbsolutePath());
			List<File> files = new ArrayList<>();
			this.listf(AppConstants.logfilePath(), files);

			System.out.println("Logfile path = " + AppConstants.logfilePath());
			
			System.out.println("#### Files ###");
        		String absPath = "";
			for (File f : files) {
				System.out.println(f.getName());
				if (f.getName().contentEquals(fileName)) {
					absPath = f.getAbsolutePath();
					break;
				}
			}
			
			if (absPath == null) {
				throw new FileNotFoundException();
			}
			
			byte[] array = Files.readAllBytes(Paths.get(absPath));
			return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/plain"))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".log" + "\"")
					.body(new ByteArrayResource(array));
        	
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            	throw e;
        }
    }
}
