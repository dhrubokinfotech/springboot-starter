package com.disl.starter.models;

import com.disl.starter.constants.AppTables.AuditModelTable;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel<U> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = AuditModelTable.ID)
	private Long id;

	@CreatedBy
	@Column(name = AuditModelTable.CREATED_BY)
	protected U createdBy;

	@LastModifiedBy
	@Column(name = AuditModelTable.LAST_MODIFIED_BY)
	protected U lastModifiedBy;

	@CreatedDate
	@Column(name = AuditModelTable.CREATION_DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	protected LocalDateTime creationDate;

	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	@Column(name = AuditModelTable.LAST_MODIFIED_DATE)
	protected LocalDateTime lastModifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonFormat(shape=JsonFormat.Shape.NUMBER, pattern="s")
	public Long getCreationDateTimeStamp() {
		if (creationDate == null) {
			return 0L;
		} else {
			return this.creationDate.toEpochSecond(OffsetDateTime.now().getOffset());
		}
	}

	@JsonFormat(shape=JsonFormat.Shape.NUMBER, pattern="s")
	public Long getLastModifiedDateTimeStamp() {
		if (lastModifiedDate == null) return 0L;
		return this.lastModifiedDate.toEpochSecond(OffsetDateTime.now().getOffset());
	}

	public U getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(U createdBy) {
		this.createdBy = createdBy;
	}

	public U getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(U lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}