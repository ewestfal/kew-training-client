package com.westbrain.training.kew.api;

import java.security.Principal;

import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.action.RequestedActions;
import org.kuali.rice.kew.api.action.ValidActions;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/documents", produces = MediaType.APPLICATION_JSON_VALUE)
public class DocumentsController {

	private WorkflowDocumentService workflowDocumentService;
	private WorkflowDocumentActionsService workflowDocumentActionsService;

	@RequestMapping("/{documentId}")
	@ResponseBody
	public DocumentWrapper document(@PathVariable String documentId, Principal principal) {
		Document document = workflowDocumentService.getDocument(documentId);
		ValidActions validActions = workflowDocumentActionsService.determineValidActions(documentId,
				principal.getName());
		RequestedActions requestedActions = workflowDocumentActionsService.determineRequestedActions(documentId,
				principal.getName());
		return new DocumentWrapper(document, validActions, requestedActions);
	}

	@RequestMapping(path = "/{documentId}/actions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void takeAction(@PathVariable String documentId, @RequestBody Action action, Principal principal) {
		WorkflowDocument workflowDocument = WorkflowDocumentFactory.loadDocument(principal.getName(), documentId);
		ActionType actionType = ActionType.fromCode(action.getActionCode());
		String annotation = action.getAnnotation();
		// unfortunately, there's not an easier way to do this, we will need to
		// map these up ourselves
		switch (actionType) {
		case COMPLETE:
			workflowDocument.complete(annotation);
			break;
		case APPROVE:
			workflowDocument.approve(annotation);
			break;
		case ACKNOWLEDGE:
			workflowDocument.acknowledge(annotation);
			break;
		case FYI:
			workflowDocument.fyi(annotation);
			break;
		case DISAPPROVE:
			workflowDocument.disapprove(annotation);
			break;
		case CANCEL:
			workflowDocument.cancel(annotation);
			break;
		case BLANKET_APPROVE:
			workflowDocument.blanketApprove(annotation);
			break;
		case SAVE:
			workflowDocument.saveDocument(annotation);
			break;
		default:
			throw new IllegalArgumentException("Invalid action type was sent: " + actionType);
		}
	}

	@Autowired
	@Required
	public void setWorkflowDocumentService(WorkflowDocumentService workflowDocumentService) {
		this.workflowDocumentService = workflowDocumentService;
	}

	@Autowired
	@Required
	public void setWorkflowDocumentActionsService(WorkflowDocumentActionsService workflowDocumentActionsService) {
		this.workflowDocumentActionsService = workflowDocumentActionsService;
	}

	static final class DocumentWrapper {

		private Document data;
		private ValidActions validActions;
		private RequestedActions requestedActions;

		DocumentWrapper(Document data, ValidActions validActions, RequestedActions requestedActions) {
			this.data = data;
			this.validActions = validActions;
			this.requestedActions = requestedActions;
		}

		public Document getData() {
			return data;
		}

		public ValidActions getValidActions() {
			return validActions;
		}

		public RequestedActions getRequestedActions() {
			return requestedActions;
		}

	}

	static class Action {

		private String actionCode;
		private String annotation;

		public String getActionCode() {
			return actionCode;
		}

		public void setActionCode(String actionCode) {
			this.actionCode = actionCode;
		}

		public String getAnnotation() {
			return annotation;
		}

		public void setAnnotation(String annotation) {
			this.annotation = annotation;
		}

	}

}
