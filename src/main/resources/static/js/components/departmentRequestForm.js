import { Grid, Row, Col, ButtonInput, Input } from 'react-bootstrap';
import React, {Component, PropTypes} from 'react';
import {reduxForm, reset, resetForm, initialize} from 'redux-form';
export const fields = ['name', 'test', 'departmentCode', 'requestText'];


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
	  console.log(JSON.stringify(values));
    fetch('/api/departmentRequests', {
      method: 'post',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        requestorName: values.name,
        departmentCode: values.departmentCode,
        requestText: values.requestText
      })
    }).then(function() {
    	that.refs.form.reset();
    	dispatch(reset('departmentRequest'));
    });
  }
  
  render() {		
    const {
      fields: {name, departmentCode, requestText, test},
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
                  <Input type="text" label="Test" {...test} />
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

export default reduxForm({
  form: 'departmentRequest',
  fields
})(DepartmentRequestForm);