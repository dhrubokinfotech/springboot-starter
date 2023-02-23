package com.disl.starter.features.cms.service;

import com.disl.starter.constants.AppUtils;
import com.disl.starter.exceptions.ResponseException;
import com.disl.starter.features.cms.dto.PageDto;
import com.disl.starter.features.cms.entity.Page;
import com.disl.starter.features.cms.entity.Section;
import com.disl.starter.features.cms.enums.PageTag;
import com.disl.starter.features.cms.model.CreateUpdatePageRequest;
import com.disl.starter.features.cms.model.CreateUpdateSectionRequest;
import com.disl.starter.features.cms.repository.PageRepository;
import com.disl.starter.models.PaginationArgs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PageService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private SectionService sectionService;

    public void createPageIfNotExists(PageTag pageTag, String title) {
        Page page = pageRepository.findTopByTag(pageTag.name());

        if (page == null) {
            page = new Page();
            page.setTitle(title);
            page.setTag(pageTag.name());
            pageRepository.save(page);
        }
    }

    public PageDto findByTagWithDto(String tag, String title, Boolean includeSections, Boolean isSortByDesc, String active) {
        PageDto pageDto = null;
        Page page = pageRepository.findTopByTag(tag);

        if (page != null) {
            List<Section> sections = new ArrayList<>();


            if (AppUtils.isTrue(includeSections)) {
                if (AppUtils.isTrue(isSortByDesc)) {
                    sections = sectionService.findAllByPageIdOrderByCreationDateDesc(page.getId());
                } else {
                    sections = sectionService.findAllByPageId(page.getId());

                }
            }

            if(AppUtils.isNotNullOrEmpty(title)){
                sections =  sections.stream().filter(section -> section.getTitle().contains(title)).collect(Collectors.toList());
            }

            if (active != null && (active.trim().equals("true") || active.trim().equals("false"))) {
                sections =  sections.stream().filter(section -> section.getActive() == Boolean.parseBoolean(active)).collect(Collectors.toList());
            }

           pageDto = getPageDto(page, sections);
        }

        return pageDto;
    }

    private List<PageDto> getPagesDtoData(List<Page> pages, Boolean includeSections) {
        List<PageDto> totalPageDtos = new ArrayList<>();

        for (Page page : pages) {
            PageDto pageDto = AppUtils.mapEntityToDto(modelMapper, page, PageDto.class);

            if (AppUtils.isTrue(includeSections)) {
                pageDto.setSections(sectionService.findAllByPageId(page.getId()));
            }

            totalPageDtos.add(pageDto);
        }

        return totalPageDtos;
    }

    public List<PageDto> findTotal(Boolean includeSections) {
        return getPagesDtoData(pageRepository.findAll(), includeSections);
    }

    public org.springframework.data.domain.Page<PageDto> getAllPagePaginated(
            PaginationArgs paginationArgs, Boolean includeSections
    ) {
        Pageable pageable = AppUtils.getPageable(paginationArgs);
        org.springframework.data.domain.Page<Page> allPages = pageRepository.findAll(pageable);
        List<PageDto> pagesDtoData = getPagesDtoData(allPages.getContent(), includeSections);
       return new PageImpl<>(pagesDtoData, pageable, allPages.getTotalElements());
    }

    public PageDto createPageSection(CreateUpdatePageRequest request) {
        String tag = request.getTag();
        if (pageRepository.existsByTag(request.getTag())) {
            throw new ResponseException("This tag (" + tag + ") already exists with other page. Please try with a different one");
        }

        Page page = getPageSavedData(new Page(), request);
        return getPageDto(page, sectionService.getSavedSectionsData(page, request, false));
    }

    public PageDto updatePageSection(CreateUpdatePageRequest request) {
        long id = request.getId();
        Page page = findPageByIdWithThrowException(request.getId());

        String tag = request.getTag();
        if(pageRepository.existsByTagAndIdNot(tag, id)) {
            throw new ResponseException("This tag (" + tag + ") already exists with other page. Please try with a different one");
        }

        page = getPageSavedData(page, request);
        return getPageDto(page, sectionService.getSavedSectionsData(page, request, true));
    }

    public Section createSectionByPage(long pageId, CreateUpdateSectionRequest request) {
        Page page = findPageByIdWithThrowException(pageId);
        return sectionService.save(sectionService.getSectionData(page.getId(), new Section(), request));
    }

    public Section updateSectionByPage(long pageId, CreateUpdateSectionRequest request) {
        Section section = sectionService.findSectionByIdAndPageIdWithThrowException(request.getId(), pageId);
        return sectionService.save(sectionService.getSectionData(pageId, section, request));
    }

    public Section findSectionByIdAndPageIdWithThrowException(Long sectionId, Long pageId) {
        return sectionService.findSectionByIdAndPageIdWithThrowException(sectionId, pageId);
    }

    public void deleteSectionByPageId(long pageId, long sectionId) {
        Section section = sectionService.findSectionByIdAndPageIdWithThrowException(sectionId, pageId);
        sectionService.delete(section);
    }

    public void deletePageById(long pageId) {
        Page page = findPageByIdWithThrowException(pageId);
        sectionService.deleteAllSectionsByPageIdAsync(page.getId());
        pageRepository.delete(page);
    }

    private Page findPageByIdWithThrowException(long id) {
        Page page = pageRepository.findById(id).orElse(null);
        if(page == null) {
            throw new ResponseException("No page found with this id: " + id);
        }

        return page;
    }

    private Page getPageSavedData(Page page, CreateUpdatePageRequest request) {
        page.setTag(request.getTag());
        page.setTitle(request.getTitle());
        page.setDescription(request.getDescription());
        return pageRepository.save(page);
    }

    private PageDto getPageDto(Page page, List<Section> sections) {
        PageDto pageDto = new PageDto();
        pageDto.setId(page.getId());
        pageDto.setTag(page.getTag());
        pageDto.setTitle(page.getTitle());
        pageDto.setCreatedBy(page.getCreatedBy());
        pageDto.setDescription(page.getDescription());
        pageDto.setCreationDate(page.getCreationDate());
        pageDto.setLastModifiedBy(page.getLastModifiedBy());
        pageDto.setLastModifiedDate(page.getLastModifiedDate());

        if(!sections.isEmpty()) {
            pageDto.setSections(sections);
        }

        return pageDto;
    }
}
