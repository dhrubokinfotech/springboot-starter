package com.disl.starter.features.cms.entity;

import com.disl.starter.constants.AppTables;
import com.disl.starter.constants.AppTables.SectionTable;
import com.disl.starter.features.db_file.entity.DbFile;
import com.disl.starter.models.AuditModel;
import jakarta.persistence.*;

@Entity
@Table(name = AppTables.sectionName)
public class Section extends AuditModel<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = SectionTable.id, nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = SectionTable.title)
    private String title;

    @Column(name = SectionTable.description, columnDefinition = "Text")
    private String description;

    @Column(name = SectionTable.externalLink, columnDefinition = "Text")
    private String externalLink;

    @Column(name = SectionTable.content, columnDefinition = "Text")
    private String content;

    @Column(name = SectionTable.pageId)
    private Long pageId;

    @Column(name = SectionTable.active)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = SectionTable.imageFileId)
    private DbFile dbFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
