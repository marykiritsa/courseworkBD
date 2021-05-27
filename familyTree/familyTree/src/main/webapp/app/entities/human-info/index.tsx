import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HumanInfo from './human-info';
import HumanInfoDetail from './human-info-detail';
import HumanInfoUpdate from './human-info-update';
import HumanInfoDeleteDialog from './human-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HumanInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HumanInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HumanInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={HumanInfo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HumanInfoDeleteDialog} />
  </>
);

export default Routes;
