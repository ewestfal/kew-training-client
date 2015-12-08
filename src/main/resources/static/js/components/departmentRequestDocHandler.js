import React, { Component } from 'react';
import { connect } from 'react-redux';

class DepartmentRequestDocHandler extends Component {
	
	componentWillMount() {		
		console.log(JSON.stringify(this.props));
	}
	
	render() {
		return (		 
	    	  <p>TODO</p>
		);
	}
}

export default connect((state) => state)(DepartmentRequestDocHandler);