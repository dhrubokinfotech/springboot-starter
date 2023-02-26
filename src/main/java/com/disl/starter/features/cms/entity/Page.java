package com.disl.starter.features.cms.entity;

import com.disl.starter.constants.AppTables;
import com.disl.starter.constants.AppTables.PageTable;
import com.disl.starter.models.AuditModel;
import jakarta.persistence.*;

@Entity
@Table(name = AppTables.PAGE_NAME)
public class Page extends AuditModel<String> {

    @Column(name = PageTable.TAG)
    private String tag;

    @Column(name = PageTable.TITLE)
    private String title;

    @Column(name = PageTable.DESCRIPTION, columnDefinition = "Text")
    private String description;

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
