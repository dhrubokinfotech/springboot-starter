package com.disl.boilerplate.models.responses;

import java.util.Date;

public class ApplicationLog {
	private String fileName;
	private Date lastModified;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}
