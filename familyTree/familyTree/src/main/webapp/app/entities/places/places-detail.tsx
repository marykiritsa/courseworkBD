import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './places.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPlacesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PlacesDetail = (props: IPlacesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { placesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="placesDetailsHeading">Places</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{placesEntity.id}</dd>
          <dt>
            <span id="placeOfBirth">Place Of Birth</span>
          </dt>
          <dd>{placesEntity.placeOfBirth}</dd>
          <dt>
            <span id="placeOfDeath">Place Of Death</span>
          </dt>
          <dd>{placesEntity.placeOfDeath}</dd>
        </dl>
        <Button tag={Link} to="/places" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/places/${placesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ places }: IRootState) => ({
  placesEntity: places.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PlacesDetail);
