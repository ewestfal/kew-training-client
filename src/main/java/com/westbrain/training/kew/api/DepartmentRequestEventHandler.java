package com.westbrain.training.kew.api;

import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class DepartmentRequestEventHandler {

	// since we don't have security built in yet, we will use the admin user
	private static final String DEFAULT_USER = "admin";
	private static final String DEPARTMENT_REQUEST_DOCUMENT_TYPE = "DepartmentRequest";
	
	@HandleBeforeCreate
	public void beforeDepartmentRequestCreate(DepartmentRequest departmentRequest) {		
		WorkflowDocument document =
				WorkflowDocumentFactory.createDocument(DEFAULT_USER, DEPARTMENT_REQUEST_DOCUMENT_TYPE);
		departmentRequest.setDocumentId(document.getDocumentId());		
	}
	
	/**
	 * Route the workflow document after the request is submitted.
	 */
	@HandleAfterCreate
	public void afterDepartmentRequest(DepartmentRequest departmentRequest) {
		WorkflowDocument document =
				WorkflowDocumentFactory.loadDocument(DEFAULT_USER, departmentRequest.getDocumentId());
		document.route("");
	}
	
	
	
}
