package com.westbrain.training.kew.api;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/departments")
public class DepartmentsController {

	@RequestMapping("")
	public JsonNode departments() throws IOException {
		return new ObjectMapper().readTree(getClass().getResourceAsStream("departments.json"));
	}
	
}
