import React, { Component, PropTypes } from 'react';
import { render } from 'react-dom';
import { Provider, connect } from 'react-redux';
import DepartmentRequestForm from './components/departmentRequestForm';

import '../../../../../node_modules/bootstrap/dist/css/bootstrap.min.css';

import thunkMiddleware from 'redux-thunk';
import createLogger from 'redux-logger';
import { createStore, applyMiddleware } from 'redux';
import { fetchDepartments } from './actions';
import rootReducer from './reducers';

const loggerMiddleware = createLogger();

const createStoreWithMiddleware = applyMiddleware(
  thunkMiddleware,
  loggerMiddleware
)(createStore);

const store = createStoreWithMiddleware(rootReducer);

class App extends Component {
	static propTypes = {
	  dispatch: PropTypes.func.isRequired,
	  departments: PropTypes.object.isRequired,
	};
	
	componentWillMount() {		
		this.props.dispatch(fetchDepartments());
	}
	
	render() {
		return (		 
	      <DepartmentRequestForm departments={this.props.departments.items} />
		);
	}
}

const ConnectedApp = connect((state) => state)(App)

render(
  <Provider store={store}>
    <ConnectedApp />
  </Provider>,
  document.getElementById('root')
);