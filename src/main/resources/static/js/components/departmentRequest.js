import { Grid, Row, Col, ButtonInput, Input } from 'react-bootstrap';
import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { reduxForm, reset, resetForm, initialize } from 'redux-form';

import { fetchDepartments } from "../actions";

export const fields = ['name', 'departmentCode', 'requestText'];

class DepartmentSelect extends Component {
	
  static propTypes: {
	  departments: PropTypes.array.isRequired;
  }
  
  render() {
	var {departments, ...inputProps} = this.props;
    var options = departments.map(dept => <option key={dept.code} value={dept.code}>{dept.name}</option>);
	return (  
      <Input type="select" label="Department" placeholder="select" {...inputProps}>
        <option key="select" value="">select</option>
        {options}
      </Input>
	);
  }
  
}

class DepartmentRequestForm extends Component {
  static propTypes = {
    departments: PropTypes.array.isRequired,
    fields: PropTypes.object.isRequired,
    handleSubmit: PropTypes.func.isRequired,
    resetForm: PropTypes.func.isRequired,
    submitting: PropTypes.bool.isRequired
  };
	
  doSubmit(values, dispatch) {
	  const that = this;
    fetch('/api/departmentRequests', {
      method: 'post',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      credentials: 'same-origin',
      body: JSON.stringify({
        requestorName: values.name,
        departmentCode: values.departmentCode,
        requestText: values.requestText
      })
    }).then(function() {
    	that.refs.form.reset();
    	dispatch(reset('departmentRequestForm'));
    });
  }
  
  render() {		
    const {
      fields: {name, departmentCode, requestText},
      handleSubmit,
      resetForm,
      submitting
    } = this.props;
    return (
      <form ref="form" onSubmit={handleSubmit(this.doSubmit.bind(this))}>
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
                  <Input type="text" label="Your Name" {...name} />
                  <DepartmentSelect departments={this.props.departments} {...departmentCode} />
                  <Input type="textarea" label="Request" rows={5} {...requestText} />
                  <ButtonInput type="submit" value="Submit" />
  	            </Col>
	          </Row>		          
	        </Col>
	      </Row>
	    </Grid>
      </form>
    );
  }
}

const DepartmentRequestReduxForm = reduxForm({
  form: 'departmentRequestForm',
  fields
})(DepartmentRequestForm);

class DepartmentRequest extends Component {
	static propTypes = {
	  dispatch: PropTypes.func.isRequired,
	  departments: PropTypes.object.isRequired,
	};
	
	componentWillMount() {
		this.props.dispatch(fetchDepartments());
	}
	
	render() {
		return (		 
	    	  <DepartmentRequestReduxForm departments={this.props.departments.items} />
		);
	}
}

export default connect((state) => state)(DepartmentRequest);


