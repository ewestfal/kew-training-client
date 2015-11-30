package com.westbrain.training.kew.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.westbrain.training.kew.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class Example05IT {

	private static final String ADMIN_PID = "admin";
	
	private static final String EXAMPLE_DOC = "PostProcessorExample";

	@Test
	public void testPostProcessor() throws Exception {
		
		// Route a new document as then admin user and attach XML to it to indicate we should follow Branch1
		
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(ADMIN_PID, EXAMPLE_DOC);
		document.route("");
		AsyncKew.waitTrue(document).isFinal();
		
		// give a bit of time to make sure the remote calls to the PP are made
		Thread.sleep(500);
		
		// use the TrackingPostProcessor to verify that the document transitioned through it's various statuses.
		// The status codes it transitions through are: I -> R -> P -> F
		// This corresponds to the following document statuses:
		//     Initiated -> Enroute -> Approved -> Processed -> Final
		// Status codes can be found using the getCode() method on org.kuali.rice.kew.api.document.DocumentStatus enumeration.	
		
		
	}
	
	
}
