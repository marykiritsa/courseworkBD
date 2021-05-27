import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Relatives from './relatives';
import RelativesDetail from './relatives-detail';
import RelativesUpdate from './relatives-update';
import RelativesDeleteDialog from './relatives-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RelativesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RelativesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RelativesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Relatives} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RelativesDeleteDialog} />
  </>
);

export default Routes;
