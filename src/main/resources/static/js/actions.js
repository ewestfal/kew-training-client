import { Promise } from 'es6-promise';

export const REQUEST_DEPARTMENTS = 'REQUEST_DEPARTMENTS';
export const RECEIVE_DEPARTMENTS = 'RECEIVE_DEPARTMENTS';
export const REQUEST_DEPARTMENT_REQUEST = 'REQUEST_DEPARTMENT_REQUEST';
export const RECEIVE_DEPARTMENT_REQUEST = 'RECEIVE_DEPARTMENT_REQUEST';

const SAME_ORIGIN = {credentials: 'same-origin'};

export function requestDepartments() {
	return {
		type: REQUEST_DEPARTMENTS
	};
}

export function receiveDepartments(json) {
	console.log(RECEIVE_DEPARTMENTS + " => " + JSON.stringify(json));
	return {
		type: RECEIVE_DEPARTMENTS,
		departments: json.departments
	};
}

export function fetchDepartments() {
	return function (dispatch) {
		dispatch(requestDepartments());
		return fetch('/api/departments', SAME_ORIGIN)
		  .then(response => response.json())
		  .then(json => dispatch(receiveDepartments(json)));
	}
}

export function requestDepartmentRequest() {
	return {
		type: REQUEST_DEPARTMENT_REQUEST
	};
}

export function receiveDepartmentRequest(json) {
	console.log(RECEIVE_DEPARTMENT_REQUEST + " => " + JSON.stringify(json));
	return {
		type: RECEIVE_DEPARTMENT_REQUEST,
		departmentRequest: json
	};
}

export function loadDepartmentRequest(documentId) {
	return function (dispatch) {
		dispatch(requestDepartmentRequest());
		return Promise.all([fetch('/api/departmentRequests/search/findByDocumentId?documentId=' + documentId, SAME_ORIGIN),
		             fetch('/api/documents/' + documentId, SAME_ORIGIN)])
		.then(responses => Promise.all([ responses[0].json(), responses[1].json() ]))
		.then(jsons => {
			const json = { departmentRequest: jsons[0], document: jsons[1] };
			return fetch('/api/departments/' + json.departmentRequest.departmentCode, SAME_ORIGIN)
			  .then(response => response.json())
			  .then(departmentJson => {
				  json.department = departmentJson;
				  dispatch(receiveDepartmentRequest(json));
			  });
		}).catch(error => console.log('Failed when attemping to load department request with document id ' + documentId, error));
	}
}

export function takeWorkflowAction(documentId, actionCode, annotation) {
	return function (dispatch) {
	return fetch('/api/documents/' + documentId + '/actions', {
	  method: 'post',
	  headers: {
	    'Accept': 'application/json',
	    'Content-Type': 'application/json'
	  },
	  credentials: 'same-origin',
	  body: JSON.stringify({
		  actionCode: actionCode,
		  annotation: annotation
	  })
	})
	  .then(function() {
		  dispatch(loadDepartmentRequest(documentId));
	  })
	  .catch(error => console.log('Failed when attempting to take workflow action with document id ' + documentId, error));
	}
}
