import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Places from './places';
import PlacesDetail from './places-detail';
import PlacesUpdate from './places-update';
import PlacesDeleteDialog from './places-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PlacesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PlacesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PlacesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Places} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PlacesDeleteDialog} />
  </>
);

export default Routes;
