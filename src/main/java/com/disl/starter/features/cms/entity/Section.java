package com.disl.starter.features.cms.entity;

import com.disl.starter.constants.AppTables;
import com.disl.starter.constants.AppTables.SectionTable;
import com.disl.starter.features.db_file.entity.DbFile;
import com.disl.starter.models.AuditModel;
import jakarta.persistence.*;

@Entity
@Table(name = AppTables.SECTION_NAME)
public class Section extends AuditModel<String> {

    @Column(name = SectionTable.TITLE)
    private String title;

    @Column(name = SectionTable.DESCRIPTION, columnDefinition = "Text")
    private String description;

    @Column(name = SectionTable.EXTERNAL_LINK, columnDefinition = "Text")
    private String externalLink;

    @Column(name = SectionTable.CONTENT, columnDefinition = "Text")
    private String content;

    @Column(name = SectionTable.PAGE_ID)
    private Long pageId;

    @Column(name = SectionTable.ACTIVE)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = SectionTable.IMAGE_FILE_ID)
    private DbFile dbFile;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public DbFile getDbFile() {
        return dbFile;
    }

    public void setDbFile(DbFile dbFile) {
        this.dbFile = dbFile;
    }
}
