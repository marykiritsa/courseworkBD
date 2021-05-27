import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Human from './human';
import HumanDetail from './human-detail';
import HumanUpdate from './human-update';
import HumanDeleteDialog from './human-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HumanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HumanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HumanDetail} />
      <ErrorBoundaryRoute path={match.url} component={Human} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HumanDeleteDialog} />
  </>
);

export default Routes;
