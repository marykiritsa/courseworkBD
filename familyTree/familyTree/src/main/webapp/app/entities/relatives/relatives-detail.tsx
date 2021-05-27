import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './relatives.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRelativesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RelativesDetail = (props: IRelativesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { relativesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="relativesDetailsHeading">Relatives</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{relativesEntity.id}</dd>
          <dt>
            <span id="surname">Surname</span>
          </dt>
          <dd>{relativesEntity.surname}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{relativesEntity.name}</dd>
          <dt>
            <span id="patronymic">Patronymic</span>
          </dt>
          <dd>{relativesEntity.patronymic}</dd>
          <dt>
            <span id="typeOfRelationship">Type Of Relationship</span>
          </dt>
          <dd>{relativesEntity.typeOfRelationship}</dd>
        </dl>
        <Button tag={Link} to="/relatives" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/relatives/${relativesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ relatives }: IRootState) => ({
  relativesEntity: relatives.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RelativesDetail);
