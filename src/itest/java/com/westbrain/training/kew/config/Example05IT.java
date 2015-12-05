package com.westbrain.training.kew.config;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.westbrain.training.kew.TrackingPostProcessor;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@WebIntegrationTest
public class Example05IT {

	private static final String ADMIN_PID = "admin";
	
	private static final String EXAMPLE_DOC = "PostProcessorExample";

	@Test
	public void testPostProcessor() throws Exception {
		
		// reset the post processor to clear out any changes that may have already been tracked
		TrackingPostProcessor.resetRouteStatusChanges();
		
		// Route a new document as then admin user and attach XML to it to indicate we should follow Branch1
		
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(ADMIN_PID, EXAMPLE_DOC);
		document.route("");
				
		// use the TrackingPostProcessor to verify that the document transitioned through it's various statuses.
		// The status codes it transitions through are: I -> R -> P -> F
		// This corresponds to the following document statuses:
		//     Initiated -> Enroute -> Approved -> Processed -> Final
		// Status codes can be found using the getCode() method on org.kuali.rice.kew.api.document.DocumentStatus enumeration.	
		
		List<DocumentRouteStatusChange> routeStatusChanges = TrackingPostProcessor.getRouteStatusChanges();
		assertEquals("There should have been 3 status changes.", 3, routeStatusChanges.size());
		
		DocumentRouteStatusChange change1 = routeStatusChanges.get(0);
		assertEquals(DocumentStatus.INITIATED.getCode(), change1.getOldRouteStatus());
		assertEquals(DocumentStatus.ENROUTE.getCode(), change1.getNewRouteStatus());
		
		DocumentRouteStatusChange change2 = routeStatusChanges.get(1);
		assertEquals(DocumentStatus.ENROUTE.getCode(), change2.getOldRouteStatus());
		assertEquals(DocumentStatus.PROCESSED.getCode(), change2.getNewRouteStatus());

		DocumentRouteStatusChange change3 = routeStatusChanges.get(2);
		assertEquals(DocumentStatus.PROCESSED.getCode(), change3.getOldRouteStatus());
		assertEquals(DocumentStatus.FINAL.getCode(), change3.getNewRouteStatus());
		
	}
	
	
}
