import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './type-of-relationship.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITypeOfRelationshipDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TypeOfRelationshipDetail = (props: ITypeOfRelationshipDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { typeOfRelationshipEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="typeOfRelationshipDetailsHeading">TypeOfRelationship</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{typeOfRelationshipEntity.id}</dd>
          <dt>
            <span id="degreeOfKinship">Degree Of Kinship</span>
          </dt>
          <dd>{typeOfRelationshipEntity.degreeOfKinship}</dd>
        </dl>
        <Button tag={Link} to="/type-of-relationship" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/type-of-relationship/${typeOfRelationshipEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ typeOfRelationship }: IRootState) => ({
  typeOfRelationshipEntity: typeOfRelationship.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TypeOfRelationshipDetail);
