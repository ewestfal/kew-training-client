import React from 'react';
import { Route } from 'react-router';
import DepartmentRequest from './components/departmentRequest';
import DepartmentRequestDocHandler from './components/departmentRequestDocHandler';

export default (
    <Route>
      <Route path="/" component={DepartmentRequest}/>
      <Route path="/docHandler" component={DepartmentRequestDocHandler}/>
    </Route>
);