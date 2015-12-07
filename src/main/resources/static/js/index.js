import React from 'react';
import { render } from 'react-dom';
import { Provider, connect } from 'react-redux';
import { Grid, Row, Col, ButtonInput, Input } from 'react-bootstrap';

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

const NameInput = React.createClass({
  getInitialState() {
    return {
      defaultValue: ''
    };
  },

  render() {
    return (
      <Input
        type="text"
        value={this.state.value}
        label="Name"/>
    );
  }
});

const DepartmentSelect = React.createClass({
  render() {
    var options = this.props.departments.map(dept => <option key={dept.code} value={dept.code}>{dept.name}</option>);
	return (  
      <Input type="select" label="Department" placeholder="select">
        {options}
      </Input>
	);
  }
});

const RequestText = React.createClass({
	  render() {
	    return (
	      <Input type="textarea" label="Text Area" rows={5}/>
	    );
	  }
});

const SubmitButton = React.createClass({
	render() {
		return (
		  <ButtonInput type="submit" value="Submit"/>
		);
	}
})

const App = React.createClass({
	componentWillMount() {
		this.props.dispatch(fetchDepartments());
	},
	render() {
		return (
	      <form>
		    <Grid>
			  <Row>
			    <Col sm={10} md={8} lg={6}>
		            <Row>
		              <Col>
		                <h1>Department Request</h1>
		              </Col>
		            </Row>
		            <Row>
		              <Col>
		                <NameInput />
		                <DepartmentSelect departments={this.props.departments.items} />
		                <RequestText />
		                <SubmitButton />
		              </Col>
		            </Row>		          
		        </Col>
		      </Row>
		    </Grid>
		  </form>
		);
	}
});

const ConnectedApp = connect((state) => state)(App)

render(
  <Provider store={store}>
    <ConnectedApp />
  </Provider>,
  document.getElementById('root')
);