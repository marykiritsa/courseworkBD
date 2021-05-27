import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './human-info.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHumanInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HumanInfoDetail = (props: IHumanInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { humanInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="humanInfoDetailsHeading">HumanInfo</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{humanInfoEntity.id}</dd>
          <dt>
            <span id="dateOfBirth">Date Of Birth</span>
          </dt>
          <dd>
            {humanInfoEntity.dateOfBirth ? (
              <TextFormat value={humanInfoEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dateOfDeath">Date Of Death</span>
          </dt>
          <dd>
            {humanInfoEntity.dateOfDeath ? (
              <TextFormat value={humanInfoEntity.dateOfDeath} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="places">Places</span>
          </dt>
          <dd>{humanInfoEntity.places}</dd>
          <dt>
            <span id="relatives">Relatives</span>
          </dt>
          <dd>{humanInfoEntity.relatives}</dd>
        </dl>
        <Button tag={Link} to="/human-info" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/human-info/${humanInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ humanInfo }: IRootState) => ({
  humanInfoEntity: humanInfo.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HumanInfoDetail);
