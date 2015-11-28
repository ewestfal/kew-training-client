package com.westbrain.training.kew.config;

import static com.westbrain.training.kew.config.AsyncKew.waitNodes;
import static com.westbrain.training.kew.config.AsyncKew.waitTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.westbrain.training.kew.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class Example04IT {

	private static final String ADMIN_PID = "admin";
	private static final String USER1_PID = "user1";
	private static final String USER2_PID = "user2";
	
	private static final String EXAMPLE_DOC = "ConditionalSplitExample";

	@Test
	public void testConditionalSplit_Branch1() throws Exception {
		
		// Route a new document as then admin user and attach XML to it to indicate we should follow Branch1
		
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(ADMIN_PID, EXAMPLE_DOC);
		document.setApplicationContent("<branchNumber>1</branchNumber>");
		document.route("");
		
		assertTrue("Document should be ENROUTE.", waitTrue(document).isEnroute());
		
		// the document should now be only at the Branch1Node
		assertTrue("Document should have been at Branch1Node", waitNodes(document, "Branch1Node"));
		Set<String> nodeNames = document.getNodeNames();
		assertEquals("Document should have 1 active nodes.", 1, nodeNames.size());
		assertTrue("Branch1Node should be the only active nodes.", nodeNames.contains("Branch1Node"));
		
		// there should be an outstanding approval request to user1 on Branch1
		
		document.switchPrincipal(USER1_PID);
		assertTrue("user1 should have an approval request.", waitTrue(document).isApprovalRequested());
		
		// approve as user1
		document.approve("Approving as user1");
		
		// document should immediately go FINAL
		
		assertTrue("Document should be FINAL.", waitTrue(document).isFinal());

	}
	
	@Test
	public void testConditionalSplit_Branch2() throws Exception {
		
		// Route a new document as then admin user and attach XML to it to indicate we should follow Branch2
		
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(ADMIN_PID, EXAMPLE_DOC);
		document.setApplicationContent("<branchNumber>2</branchNumber>");
		document.route("");
		
		assertTrue("Document should be ENROUTE.", waitTrue(document).isEnroute());
		
		// the document should now be only at the Branch2Node
		assertTrue("Document should have been at Branch2Node", waitNodes(document, "Branch2Node"));
		Set<String> nodeNames = document.getNodeNames();
		assertEquals("Document should have 1 active nodes.", 1, nodeNames.size());
		assertTrue("Branch2Node should be the only active nodes.", nodeNames.contains("Branch2Node"));
		
		// there should be an outstanding approval request to user2 on Branch2
		
		document.switchPrincipal(USER2_PID);
		assertTrue("user2 should have an approval request.", waitTrue(document).isApprovalRequested());
		
		// approve as user2
		document.approve("Approving as user2");
		
		// document should immediately go FINAL
		
		assertTrue("Document should be FINAL.", waitTrue(document).isFinal());

	}
	
}
