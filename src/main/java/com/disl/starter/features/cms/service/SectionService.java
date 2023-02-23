package com.disl.starter.features.cms.service;

import com.disl.starter.constants.AppUtils;
import com.disl.starter.exceptions.ResponseException;
import com.disl.starter.features.cms.entity.Page;
import com.disl.starter.features.cms.entity.Section;
import com.disl.starter.features.cms.model.CreateUpdatePageRequest;
import com.disl.starter.features.cms.model.CreateUpdateSectionRequest;
import com.disl.starter.features.cms.repository.SectionRepository;
import com.disl.starter.features.db_file.entity.DbFile;
import com.disl.starter.features.db_file.service.DbFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SectionService {

    @Autowired
    private DbFileService dbFileService;

    @Autowired
    private SectionRepository sectionRepository;

    public Section save(Section section) {
        return sectionRepository.save(section);
    }

    public void delete(Section section) {
        dbFileService.deleteDbFileAsync(section.getDbFile());
        sectionRepository.delete(section);
    }

    public List<Section> findAllByPageId(Long pageId) {
        return sectionRepository.findAllByPageId(pageId);
    }

    public List<Section> findAllByPageIdOrderByCreationDateDesc(Long pageId) {
        return sectionRepository.findAllByPageIdOrderByCreationDateDesc(pageId);
    }

    public Section findSectionByIdAndPageIdWithThrowException(Long id, Long pageId) {
        Section section = sectionRepository.findTopByIdAndPageId(id, pageId);
        if (section == null) {
            throw new ResponseException("No section found with this id: " + id + " and page id: " + pageId);
        }

        return section;
    }

    public List<Section> getSavedSectionsData(Page page, CreateUpdatePageRequest request, boolean isFromUpdate) {
        List<Section> sections = new ArrayList<>();
        Set<CreateUpdateSectionRequest> sectionRequestSet = request.getCreateUpdateSectionRequests();

        for (CreateUpdateSectionRequest secRequest : sectionRequestSet) {
            Section section;

            if(isFromUpdate) {
                section = findSectionByIdAndPageIdWithThrowException(secRequest.getId(), request.getId());
                section = getSectionData(page.getId(), section, secRequest);
            } else {
                section = getSectionData(page.getId(), new Section(), secRequest);
            }

            sections.add(section);
        }

        if (!sections.isEmpty()) {
            sections = sectionRepository.saveAll(sections);
        }

        return sections;
    }

    public Section getSectionData(Long pageId, Section section, CreateUpdateSectionRequest request) {
        section.setPageId(pageId);
        section.setTitle(request.getTitle());
        section.setContent(request.getContent().trim());
        section.setExternalLink(request.getExternalLink());
        section.setDescription(request.getDescription().trim());
        section.setActive(request.getActive());

        Long reqImageFileId = request.getImageFileId();
        if (AppUtils.isNotNullAndGreaterZero(reqImageFileId)) {
            DbFile dbFile = dbFileService.findByIdWithThrowException(reqImageFileId);

            if (AppUtils.isNotNullAndGreaterZero(section.getId())) {
                DbFile prevDbFile = section.getDbFile();
                dbFileService.deletePreviousDbFileAsync(prevDbFile, dbFile);
            }

            section.setDbFile(dbFile);
        }else{

            section.setDbFile(null);
        }

        return section;
    }

    @Async
    public void deleteAllSectionsByPageIdAsync(Long pageId) {
        List<Section> sections = sectionRepository.findAllByPageId(pageId);
        for (Section section : sections) {
            dbFileService.deleteDbFileAsync(section.getDbFile());
            sectionRepository.delete(section);
        }
    }
}
