import { applyMiddleware, createStore, combineReducers, compose } from 'redux';
import createLogger from 'redux-logger';
import thunkMiddleware from 'redux-thunk';
import { reduxReactRouter } from 'redux-router';
import { createHistory } from 'history';

import rootReducer from './reducers';

export default function create(routes) {
    const finalCreateStore = compose(
    	applyMiddleware(
    		thunkMiddleware,
    		createLogger()
    	),
    	reduxReactRouter({
            routes,
            createHistory
        }))(createStore);
    return finalCreateStore(rootReducer);
}