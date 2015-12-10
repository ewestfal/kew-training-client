import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import { routerStateReducer } from 'redux-router';
import { REQUEST_DEPARTMENTS, RECEIVE_DEPARTMENTS, REQUEST_DEPARTMENT_REQUEST, RECEIVE_DEPARTMENT_REQUEST } from './actions';

function departments(state = {
  isFetching: false,
  items: []
}, action) {
	switch (action.type) {
      case REQUEST_DEPARTMENTS:
        return Object.assign({}, state, {
	        isFetching: true
        });
	  case RECEIVE_DEPARTMENTS:
	    return Object.assign({}, state, {
	    	isFetching: false,
	    	items: action.departments
	    });		  
	  default:
		  return state;
	}
}

function departmentRequest(state = {
  isFetching: false,
  departmentRequest: null,
  department: null,
  document: null
}, action) {
	switch (action.type) {
		case REQUEST_DEPARTMENT_REQUEST:
		  return Object.assign({}, state, {
			  isFetching: true
		  });
		case RECEIVE_DEPARTMENT_REQUEST:
		  return Object.assign({}, state, {
			  isFetching: false,
			  departmentRequest: action.departmentRequest.departmentRequest,
			  department: action.departmentRequest.department,
		      document: action.departmentRequest.document
		  });
		default:
		  return state;
	}
}

export default combineReducers({
	departments,
	departmentRequest,
	form: formReducer,
	router: routerStateReducer
});
