import React, { Component } from 'react';
import { Button, ButtonToolbar } from 'react-bootstrap';
import { connect } from 'react-redux';

import { takeWorkflowAction } from '../actions';

class WorkflowButton extends Component {
	handleClick(event) {
      this.props.dispatch(takeWorkflowAction(this.props.documentId, this.props.action, ''));
	}
	render() {
		return (
			<Button onClick={this.handleClick.bind(this)}>{this.props.children}</Button>
		);
	}
}

const ConnectedWorkflowButton = connect((state) => state)(WorkflowButton);


export class WorkflowButtons extends Component {
	render() {
		const requestedActions = this.props.document.requestedActions;
		const documentId = this.props.document.data.documentId;
		const buttons = [];		
		if (requestedActions.completeRequested) buttons.push(<ConnectedWorkflowButton key='C' action='C' documentId={documentId}>Complete</ConnectedWorkflowButton>);
		if (requestedActions.approveRequested) buttons.push(<ConnectedWorkflowButton key='A' action='A' documentId={documentId}>Approve</ConnectedWorkflowButton>);
		if (requestedActions.acknowledgeRequested) buttons.push(<ConnectedWorkflowButton key='K' action='K' documentId={documentId}>Acknowledge</ConnectedWorkflowButton>);
		if (requestedActions.fyiRequested) buttons.push(<ConnectedWorkflowButton key='F' action='F' documentId={documentId}>FYI</ConnectedWorkflowButton>);
		let buttonBar = <em>No actions available at this time</em>
		if (buttons.length > 0) {
			buttonBar = (
		      <ButtonToolbar>
		        {buttons}
		      </ButtonToolbar>
		    );
		}
		return buttonBar;
	}
	
}