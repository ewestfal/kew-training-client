export const REQUEST_DEPARTMENTS = 'REQUEST_DEPARTMENTS';
export const RECEIVE_DEPARTMENTS = 'RECEIVE_DEPARTMENTS';

export function requestDepartments() {
	return {
		type: REQUEST_DEPARTMENTS
	};
}

export function receiveDepartments(json) {
	return {
		type: RECEIVE_DEPARTMENTS,
		departments: json.departments
	};
}

export function fetchDepartments() {
	return function (dispatch) {
		dispatch(requestDepartments());
		return fetch('/api/departments')
		  .then(response => response.json())
		  .then(json => dispatch(receiveDepartments(json)));
	}
}