import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import { ReduxRouter } from 'redux-router';

import routes from './routes';
import createStore from './create-store';

import '../../../../../node_modules/bootstrap/dist/css/bootstrap.min.css';

const store = createStore(routes);

render(
  <Provider store={store}>
    <ReduxRouter>
      {routes}
    </ReduxRouter>
  </Provider>,
  document.getElementById('root')
);