package com.westbrain.training.kew.api;

import javax.persistence.PostLoad;

import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentStatus;

public class WorkflowDocumentEntityListener {
		
	@PostLoad
	public void postLoad(DepartmentRequest departmentRequest) {
		DocumentStatus documentStatus =
				KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(departmentRequest.getDocumentId());
		departmentRequest.setRequestStatus(documentStatus);
	}

}
