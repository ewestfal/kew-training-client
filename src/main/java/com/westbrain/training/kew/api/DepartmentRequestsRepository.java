package com.westbrain.training.kew.api;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

@Transactional
public interface DepartmentRequestsRepository extends PagingAndSortingRepository<DepartmentRequest, Long>{}
