package com.westbrain.training.kew;

import java.util.Iterator;

import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.IDocumentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class PostProcessorController {
		
	@Autowired
	private TrackingPostProcessor trackingPostProcessor;
	
	@RequestMapping("/postprocessor")
	public void handleEvents(@RequestBody JsonNode json) throws Exception {
		Iterator<JsonNode> iterator = json.get("events").iterator();
		while (iterator.hasNext()) {
			JsonNode event = iterator.next();
			String documentEventCode = event.get("documentEventCode").asText();
			if (documentEventCode.equals(IDocumentEvent.ROUTE_LEVEL_CHANGE)) {
				DocumentRouteLevelChange routeLevelChange = new DocumentRouteLevelChange(
						asText(event.get("documentId")),
						asText(event.get("appDocId")),
						asInteger(event.get("oldRouteLevel")),
						asInteger(event.get("newRouteLevel")),
						asText(event.get("oldNodeName")),
						asText(event.get("newNodeName")),
						asText(event.get("oldNodeInstanceId")),
						asText(event.get("newNodeInstanceId")));
				handleRouteLevelChange(routeLevelChange);
			} else if (documentEventCode.equals(IDocumentEvent.ROUTE_STATUS_CHANGE)) {
				DocumentRouteStatusChange routeStatusChange = new DocumentRouteStatusChange(
						asText(event.get("documentId")),
						asText(event.get("appDocId")),
						asText(event.get("oldRouteStatus")),
						asText(event.get("newRouteStatus")));
				handleRouteStatusChange(routeStatusChange);				
			} else {
				throw new IllegalArgumentException("Unrecognized document event code: " + documentEventCode);
			}
		}
	}
	
	private String asText(JsonNode node) {
		return node == null ? null : node.asText();
	}
	
	private Integer asInteger(JsonNode node) {
		return node == null ? null : node.asInt();
	}
	
	protected void handleRouteLevelChange(DocumentRouteLevelChange routeLevelChange) throws Exception {
		trackingPostProcessor.doRouteLevelChange(routeLevelChange);
	}
	
	protected void handleRouteStatusChange(DocumentRouteStatusChange routeStatusChange) throws Exception {
		trackingPostProcessor.doRouteStatusChange(routeStatusChange);
	}

}
