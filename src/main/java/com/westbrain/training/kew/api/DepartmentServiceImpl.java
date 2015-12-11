package com.westbrain.training.kew.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {

	@Override
	public List<Department> getAllDepartments() {
		List<Department> departments = new ArrayList<Department>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.reader().readTree(getClass().getResourceAsStream("departments.json"));		
			for (Iterator<JsonNode> iterator = jsonNode.get("departments").iterator(); iterator.hasNext(); ) {
				JsonNode departmentNode = iterator.next();
				Department department = new Department();
				department.setCode(departmentNode.get("code").asText());
				department.setName(departmentNode.get("name").asText());
				department.setDepartmentHead(departmentNode.get("departmentHead").asText());
				departments.add(department);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse and read JSON for departments.");
		}
		return departments;
	}
	
	@Override
	public Department getDepartmentByCode(String departmentCode) {
		for (Department department : getAllDepartments()) {
			if (department.getCode().equals(departmentCode)) {
				return department;
			}
		}
		return null;
	}

}
