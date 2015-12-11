package com.westbrain.training.kew.api;

import java.util.List;

public interface DepartmentService {

	List<Department> getAllDepartments();
	
	Department getDepartmentByCode(String departmentCode);
	
}
