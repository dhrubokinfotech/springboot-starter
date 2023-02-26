package com.disl.starter.features.db_file.entity;

import com.disl.starter.constants.AppTables;
import com.disl.starter.constants.AppTables.DbFileTable;
import com.disl.starter.constants.AppUtils;
import com.disl.starter.features.db_file.enums.FileUploadType;
import com.disl.starter.models.AuditModel;
import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

import static com.disl.starter.constants.AppConstants.DB_FILE_BASE_UR;

@Entity
@Table(name = AppTables.DB_FILE_NAME)
public class DbFile extends AuditModel<String> {

    @Column(name = DbFileTable.FILE_NAME)
    private String fileName;

    @Column(name = DbFileTable.FILE_TYPE)
    private String fileType;

    @Column(name = DbFileTable.FILE_EXTENSION)
    private String fileExtension;

    @Column(name = DbFileTable.MIME_TYPE)
    private String mimeType;

    @Column(name = DbFileTable.FILE_KEY)
    private String fileKey;

    @Enumerated(EnumType.STRING)
    @Column(name = DbFileTable.UPLOAD_TYPE)
    private FileUploadType uploadType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public FileUploadType getUploadType() {
        return uploadType;
    }

    public void setUploadType(FileUploadType uploadType) {
        this.uploadType = uploadType;
    }

    public String getFileUrl() {
        if(AppUtils.isNotNullOrEmpty(fileKey)){
            return StringUtils.removeEnd(DB_FILE_BASE_UR, "/") + "/" + fileKey;
        }

       return null;
    }
}
