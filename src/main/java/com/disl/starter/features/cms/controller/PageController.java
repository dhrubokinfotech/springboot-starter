package com.disl.starter.features.cms.controller;

import com.disl.starter.config.CommonApiResponses;
import com.disl.starter.enums.AscOrDescType;
import com.disl.starter.features.cms.dto.PageDto;
import com.disl.starter.features.cms.enums.PageTag;
import com.disl.starter.features.cms.entity.Page;
import com.disl.starter.features.cms.entity.Section;
import com.disl.starter.features.cms.model.CreateUpdatePageRequest;
import com.disl.starter.features.cms.model.CreateUpdateSectionRequest;
import com.disl.starter.features.cms.service.PageService;
import com.disl.starter.models.PaginationArgs;
import com.disl.starter.models.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.disl.starter.constants.AppConstants.*;

@RestController
@CommonApiResponses
@RequestMapping("/api/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @Operation(summary = "find a page-section")
    @ApiResponse(content = @Content(schema = @Schema(implementation = PageDto.class)), responseCode = "200")
    @GetMapping("/tag/{tag}")
    public ResponseEntity<Response> findByTag(
            @PathVariable("tag") String tag,
            @RequestParam(value = "includeSections", required = false) Boolean includeSections,
            @RequestParam(value = "title", defaultValue = "", required = false) String title,
            @RequestParam(value = "sortByDesc",  required = false) Boolean isSortByDesc,
            @RequestParam(required = false) String active
    ) {
        PageDto page = pageService.findByTagWithDto(tag.trim(), title, includeSections, isSortByDesc, active);
        if(page == null) {
            return Response.getResponseEntity(false, "Page not found with this tag");
        }

        return Response.getResponseEntity(true, "Page loaded with this tag", page);
    }

    @Operation(summary = "Create new page with sections", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = PageDto.class)), responseCode = "200")
    @PreAuthorize("hasAuthority('PAGE_CREATE')")
    @PostMapping("/create")
    public ResponseEntity<Response> create(@Valid @RequestBody CreateUpdatePageRequest request) {
       return Response.getResponseEntity(
               true, "Page/section created",
               pageService.createPageSection(request)
       );
    }

    @Operation(summary = "update page with sections", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = PageDto.class)), responseCode = "200")
    @PreAuthorize("hasAuthority('PAGE_UPDATE')")
    @PutMapping("/update")
    public ResponseEntity<Response> update(@Valid @RequestBody CreateUpdatePageRequest request) {
        return Response.getResponseEntity(
                true, "Page/section updated",
                pageService.updatePageSection(request)
        );
    }

    @Operation(summary = "add a new section into a page", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Section.class)), responseCode = "200")
    @PreAuthorize("hasAuthority('PAGE_CREATE')")
    @PostMapping("/{pageId}/section/add")
    public ResponseEntity<Response> addSectionIntoPage(
            @PathVariable("pageId") long pageId,
            @Valid @RequestBody CreateUpdateSectionRequest request
    ) {
        return Response.getResponseEntity(
                true, "New section added to the page",
                pageService.createSectionByPage(pageId, request)
        );
    }

    @Operation(summary = "update a section by page id", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Section.class)), responseCode = "200")
    @PreAuthorize("hasAuthority('PAGE_UPDATE')")
    @PutMapping("/{pageId}/section/update")
    public ResponseEntity<Response> updateSectionByPage(
            @PathVariable("pageId") long pageId,
            @Valid @RequestBody CreateUpdateSectionRequest request
    ) {
        return Response.getResponseEntity(
                true, "Section updated to the page",
                pageService.updateSectionByPage(pageId, request)
        );
    }

    @Operation(summary = "delete a specific section by page id", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200")
    @PreAuthorize("hasAuthority('PAGE_DELETE')")
    @DeleteMapping("/{pageId}/delete/{sectionId}")
    public ResponseEntity<Response> deleteSectionByPage(
            @PathVariable("pageId") long pageId,
            @PathVariable("sectionId") long sectionId
    ) {
        pageService.deleteSectionByPageId(pageId, sectionId);
        return Response.getResponseEntity(true, "Section deleted successfully");
    }

    @Operation(summary = "get a specific section by section id and page id", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200")
    @GetMapping("/{pageId}/section/id/{sectionId}")
    public ResponseEntity<Response> findSectionById(
            @PathVariable("pageId") long pageId,
            @PathVariable("sectionId") long sectionId
    ) {
        Section section = pageService.findSectionByIdAndPageIdWithThrowException(sectionId, pageId);
        return Response.getResponseEntity(true, "Section data loaded", section);
    }

    @Operation(summary = "delete a page", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(schema = @Schema(implementation = Boolean.class)), responseCode = "200")
    @PreAuthorize("hasAuthority('PAGE_DELETE')")
    @DeleteMapping("/delete/{pageId}")
    public ResponseEntity<Response> deletePage(@PathVariable("pageId") long pageId) {
        pageService.deletePageById(pageId);
        return Response.getResponseEntity(true, "Page deleted successfully");
    }

    @Operation(summary = "get all page - paginated", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Page.class))), responseCode = "200")
    @PreAuthorize("hasAuthority('PAGE_READ')")
    @GetMapping("/all")
    public Response getAllPagePaginated(
            @RequestParam(name = PAGE_NO, defaultValue = "0") int pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = "20") int pageSize,
            @RequestParam(name = SORT_BY, defaultValue = "") String sortBy,
            @RequestParam(name = ASC_OR_DESC, defaultValue = "") AscOrDescType ascOrDesc,
            @RequestParam(value = "includeSections", required = false) Boolean includeSections
    ) {
        PaginationArgs paginationArgs = new PaginationArgs(
                pageNo, pageSize, sortBy, ascOrDesc
        );

        return new Response(
                HttpStatus.OK, true, "All paginated pages loaded",
                pageService.getAllPagePaginated(paginationArgs, includeSections)
        );
    }

    @Operation(summary = "get total page - without paginated", security = @SecurityRequirement(name = "jwtToken"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Page.class))), responseCode = "200")
    @PreAuthorize("hasAuthority('PAGE_READ')")
    @GetMapping("/total")
    public Response getTotal(@RequestParam(value = "includeSections", required = false) Boolean includeSections) {
        return new Response(
                HttpStatus.OK, true, "Total pages loaded",
                pageService.findTotal(includeSections)
        );
    }

    @Operation(summary = "get predefined tags")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = PageTag.class))), responseCode = "200")
    @GetMapping("/predefine-tags")
    public ResponseEntity<Response> predefineTags() {
        return Response.getResponseEntity(true, "Predefine tags loaded", PageTag.values());
    }
}
