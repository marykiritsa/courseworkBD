import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './human-info.reducer';
import { IHumanInfo } from 'app/shared/model/human-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHumanInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HumanInfoUpdate = (props: IHumanInfoUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { humanInfoEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/human-info' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...humanInfoEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="familyTreeApp.humanInfo.home.createOrEditLabel" data-cy="HumanInfoCreateUpdateHeading">
            Create or edit a HumanInfo
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : humanInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="human-info-id">ID</Label>
                  <AvInput id="human-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="dateOfBirthLabel" for="human-info-dateOfBirth">
                  Date Of Birth
                </Label>
                <AvField id="human-info-dateOfBirth" data-cy="dateOfBirth" type="date" className="form-control" name="dateOfBirth" />
              </AvGroup>
              <AvGroup>
                <Label id="dateOfDeathLabel" for="human-info-dateOfDeath">
                  Date Of Death
                </Label>
                <AvField id="human-info-dateOfDeath" data-cy="dateOfDeath" type="date" className="form-control" name="dateOfDeath" />
              </AvGroup>
              <AvGroup>
                <Label id="placesLabel" for="human-info-places">
                  Places
                </Label>
                <AvField
                  id="human-info-places"
                  data-cy="places"
                  type="string"
                  className="form-control"
                  name="places"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    min: { value: 0, errorMessage: 'This field should be at least 0.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="relativesLabel" for="human-info-relatives">
                  Relatives
                </Label>
                <AvField
                  id="human-info-relatives"
                  data-cy="relatives"
                  type="string"
                  className="form-control"
                  name="relatives"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    min: { value: 0, errorMessage: 'This field should be at least 0.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/human-info" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  humanInfoEntity: storeState.humanInfo.entity,
  loading: storeState.humanInfo.loading,
  updating: storeState.humanInfo.updating,
  updateSuccess: storeState.humanInfo.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HumanInfoUpdate);
