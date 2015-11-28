package com.westbrain.training.kew.config;

import static com.westbrain.training.kew.config.AsyncKew.waitFalse;
import static com.westbrain.training.kew.config.AsyncKew.waitNodes;
import static com.westbrain.training.kew.config.AsyncKew.waitTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class Example03IT {

	private static final String ADMIN_PID = "admin";
	private static final String USER1_PID = "user1";
	private static final String USER2_PID = "user2";
	
	private static final String EXAMPLE_DOC = "SplitExample";

	@Test
	public void testSplit() throws Exception {
		
		// Create a new document as then admin user and then route it, should immediately pass through the split
		// and initiate branching
		
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(ADMIN_PID, EXAMPLE_DOC);
		document.route("");
		
		assertTrue("Document should be ENROUTE.", waitTrue(document).isEnroute());
		
		// the document should now be at both the Branch1Node and the Branch2Node, verify this is the case
		
		assertTrue("Document should have been at Branch1Node and Branch2Node", waitNodes(document, "Branch1Node", "Branch2Node"));
		Set<String> nodeNames = document.getNodeNames();
		assertEquals("Document should have 2 active nodes.", 2, nodeNames.size());
		assertTrue("Branch1Node should be one of the active nodes.", nodeNames.contains("Branch1Node"));
		assertTrue("Branch2Node should be one of the active nodes.", nodeNames.contains("Branch2Node"));
		
		// since the document is sitting at both nodes, there should be approval requests for both user1 and user2
		
		document.switchPrincipal(USER1_PID);
		assertTrue("user1 should have an approval request.", waitTrue(document).isApprovalRequested());
		
		document.switchPrincipal(USER2_PID);
		assertTrue("user2 should have an approval request.", waitTrue(document).isApprovalRequested());
		
		// approve as user2
		document.approve("Approving as user2");
		
		// user2 should no longer have an outstanding approve request
		assertFalse("user2 should no longer have an outstanding approval request.", waitFalse(document).isApprovalRequested());
		
		// document should now transition out of Branch2 into the join node, active nodes should now be Branch1Node and SimpleJoin
		assertTrue("Document should have been at Branch1Node and SimpleJoin", waitNodes(document, "Branch1Node", "SimpleJoin"));
		nodeNames = document.getNodeNames();
		assertTrue("Branch1Node should be one of the active nodes.", nodeNames.contains("Branch1Node"));
		assertTrue("SimpleJoin should be one of the active nodes.", nodeNames.contains("SimpleJoin"));
		
		// the document should still be ENROUTE
		assertTrue("Document should be ENROUTE.", waitTrue(document).isEnroute());
		
		// now approve as user1 to complete Branch1, this will cause the document to transition out of the
		// SimpleJoin node and into the FINAL state
		
		document.switchPrincipal(USER1_PID);
		assertTrue("user1 should have an approval request.", waitTrue(document).isApprovalRequested());
		document.approve("Approving as user1");
		
		// document should be FINAL now
		
		assertTrue("Document should be FINAL.", waitTrue(document).isFinal());

	}
	
}
