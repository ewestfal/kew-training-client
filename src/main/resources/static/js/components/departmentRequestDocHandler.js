import React, { Component } from 'react';
import { connect } from 'react-redux';
import { loadDepartmentRequest } from '../actions';

import { WorkflowButtons } from './workflowButtons';

class DepartmentRequestDocHandler extends Component {
	
	componentWillMount() {
		this.props.dispatch(loadDepartmentRequest(this.props.location.query.docId));
	}
	
	render() {
		const departmentRequest = this.props.departmentRequest;
		if (departmentRequest.departmentRequest != null) {
			const { requestorName, department, requestText } = departmentRequest.departmentRequest; 
			const { documentId, status } = departmentRequest.document.data;
			return (
			  <div className="container">
			    <h1>Department Request</h1>
			    <dl className="dl-horizontal">
			      <dt>Requestor Name</dt>
			      <dd>{requestorName}</dd>
			      <dt>Department</dt>
	    	      <dd>{departmentRequest.department.name}</dd>
	    	      <dt>Request Text</dt>
	    	      <dd>{requestText}</dd>
	    	      <dt>Document ID</dt>
	    	      <dd>{documentId}</dd>
	    	      <dt>Document Status</dt>
	    	      <dd>{status}</dd>
			    </dl>
			    <WorkflowButtons document={departmentRequest.document}/>
			  </div>
		    );
	    } else {
	    	return <div className="container">Loading...</div>	
	    }
	}

}

export default connect((state) => state)(DepartmentRequestDocHandler);