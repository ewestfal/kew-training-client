package com.westbrain.training.kew.api;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/api/departments", produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentsController {

	@RequestMapping("")
	public JsonNode departments() throws IOException {
		return new ObjectMapper().readTree(getClass().getResourceAsStream("departments.json"));
	}
	
	@RequestMapping("/{code}")
	public ResponseEntity<?> department(@PathVariable String code) throws IOException {
		for (Iterator<JsonNode> iterator = departments().get("departments").iterator(); iterator.hasNext(); ) {
			JsonNode departmentNode = iterator.next();
			if (departmentNode.get("code").asText().equals(code)) {
				return ResponseEntity.ok(departmentNode);
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	
	
}
