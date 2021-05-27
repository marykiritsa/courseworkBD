import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Human from './human';
import HumanInfo from './human-info';
import Relatives from './relatives';
import TypeOfRelationship from './type-of-relationship';
import Places from './places';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}human`} component={Human} />
      <ErrorBoundaryRoute path={`${match.url}human-info`} component={HumanInfo} />
      <ErrorBoundaryRoute path={`${match.url}relatives`} component={Relatives} />
      <ErrorBoundaryRoute path={`${match.url}type-of-relationship`} component={TypeOfRelationship} />
      <ErrorBoundaryRoute path={`${match.url}places`} component={Places} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
