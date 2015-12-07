import { combineReducers } from 'redux';
import { REQUEST_DEPARTMENTS, RECEIVE_DEPARTMENTS } from './actions';

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

export default combineReducers({
	departments
});
