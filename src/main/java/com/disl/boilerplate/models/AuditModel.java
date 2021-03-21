package com.disl.boilerplate.models;


import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

public abstract class AuditModel<U> {

	@CreatedBy
	protected U createdBy;

	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	protected LocalDateTime creationDate;

	@LastModifiedBy
	protected U lastModifiedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	@LastModifiedDate
	protected LocalDateTime lastModifiedDate;
	
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