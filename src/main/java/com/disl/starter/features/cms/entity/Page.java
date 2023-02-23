package com.disl.starter.features.cms.entity;

import com.disl.starter.constants.AppTables;
import com.disl.starter.constants.AppTables.PageTable;
import com.disl.starter.models.AuditModel;
import jakarta.persistence.*;

@Entity
@Table(name = AppTables.pageName)
public class Page extends AuditModel<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = PageTable.id, nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = PageTable.tag)
    private String tag;

    @Column(name = PageTable.title)
    private String title;

    @Column(name = PageTable.description, columnDefinition = "Text")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
