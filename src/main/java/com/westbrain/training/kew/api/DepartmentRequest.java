package com.westbrain.training.kew.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.kuali.rice.kew.api.document.DocumentStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@EntityListeners(WorkflowDocumentEntityListener.class)
public class DepartmentRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonIgnore
	@Column(nullable = false)
	private String documentId;
	
	@NotNull
	private String requestorName;
	
	@NotNull
	private String departmentCode;
	
	@NotNull
	private String requestText;
	
	@Transient
	@JsonIgnore
	private DocumentStatus requestStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty
	public String getDocumentId() {
		return documentId;
	}

	@JsonIgnore
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getRequestorName() {
		return requestorName;
	}

	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getRequestText() {
		return requestText;
	}

	public void setRequestText(String requestText) {
		this.requestText = requestText;
	}

	@JsonProperty
	public DocumentStatus getRequestStatus() {
		return requestStatus;
	}

	@JsonIgnore
	public void setRequestStatus(DocumentStatus requestStatus) {
		this.requestStatus = requestStatus;
	}
	
	
	
}
