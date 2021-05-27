import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './human.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHumanDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HumanDetail = (props: IHumanDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { humanEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="humanDetailsHeading">Human</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{humanEntity.id}</dd>
          <dt>
            <span id="surname">Surname</span>
          </dt>
          <dd>{humanEntity.surname}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{humanEntity.name}</dd>
          <dt>
            <span id="patronymic">Patronymic</span>
          </dt>
          <dd>{humanEntity.patronymic}</dd>
          <dt>
            <span id="humanInfo">Human Info</span>
          </dt>
          <dd>{humanEntity.humanInfo}</dd>
        </dl>
        <Button tag={Link} to="/human" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/human/${humanEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ human }: IRootState) => ({
  humanEntity: human.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HumanDetail);
