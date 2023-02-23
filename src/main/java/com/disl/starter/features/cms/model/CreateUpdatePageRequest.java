package com.disl.starter.features.cms.model;

import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class CreateUpdatePageRequest {

    @NotBlank
    private String tag;

    @NotBlank
    private String title;

    private long id;
    private String description;
    private Set<CreateUpdateSectionRequest> createUpdateSectionRequests = new HashSet<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<CreateUpdateSectionRequest> getCreateUpdateSectionRequests() {
        return createUpdateSectionRequests;
    }

    public void setCreateUpdateSectionRequests(Set<CreateUpdateSectionRequest> createUpdateSectionRequests) {
        this.createUpdateSectionRequests = createUpdateSectionRequests;
    }
}
