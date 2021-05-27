import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TypeOfRelationship from './type-of-relationship';
import TypeOfRelationshipDetail from './type-of-relationship-detail';
import TypeOfRelationshipUpdate from './type-of-relationship-update';
import TypeOfRelationshipDeleteDialog from './type-of-relationship-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TypeOfRelationshipUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TypeOfRelationshipUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TypeOfRelationshipDetail} />
      <ErrorBoundaryRoute path={match.url} component={TypeOfRelationship} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TypeOfRelationshipDeleteDialog} />
  </>
);

export default Routes;
