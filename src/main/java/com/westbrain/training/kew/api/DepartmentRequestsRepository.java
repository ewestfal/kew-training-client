package com.westbrain.training.kew.api;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface DepartmentRequestsRepository extends PagingAndSortingRepository<DepartmentRequest, Long> {
	
	DepartmentRequest findByDocumentId(@Param("documentId") String documentId);
	
}
