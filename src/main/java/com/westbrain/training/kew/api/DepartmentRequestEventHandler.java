package com.westbrain.training.kew.api;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RepositoryEventHandler
public class DepartmentRequestEventHandler {

	private static final Logger LOG = Logger.getLogger(DepartmentRequestEventHandler.class);
	private static final String DEPARTMENT_REQUEST_DOCUMENT_TYPE = "DepartmentRequest";
		
	@HandleBeforeCreate
	public void beforeDepartmentRequestCreate(DepartmentRequest departmentRequest) {	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		WorkflowDocument document =
				WorkflowDocumentFactory.createDocument(authentication.getName(), DEPARTMENT_REQUEST_DOCUMENT_TYPE);
		departmentRequest.setDocumentId(document.getDocumentId());		
	}
	
	/**
	 * Route the workflow document after the request is submitted.
	 */
	@HandleAfterCreate
	public void afterDepartmentRequest(DepartmentRequest departmentRequest) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		WorkflowDocument document =
				WorkflowDocumentFactory.loadDocument(authentication.getName(), departmentRequest.getDocumentId());
		String xml = toWorkflowXml(departmentRequest);
		LOG.info("Routing departmentRequest with XML representation: " + xml);
		document.setApplicationContent(xml);
		document.route("");
	}
	
	private String toWorkflowXml(DepartmentRequest departmentRequest) throws IOException {
		String jsonValue = new ObjectMapper().writer().withRootName("departmentRequest").writeValueAsString(departmentRequest);
		JSONObject json = new JSONObject(jsonValue);
		// remove requestStatus since it is derived and will always be null
		json.getJSONObject("departmentRequest").remove("requestStatus");
		return XML.toString(json);
	}

}
