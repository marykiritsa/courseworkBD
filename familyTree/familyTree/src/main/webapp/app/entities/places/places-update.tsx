import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './places.reducer';
import { IPlaces } from 'app/shared/model/places.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPlacesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PlacesUpdate = (props: IPlacesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { placesEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/places' + props.location.search);
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
        ...placesEntity,
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
          <h2 id="familyTreeApp.places.home.createOrEditLabel" data-cy="PlacesCreateUpdateHeading">
            Create or edit a Places
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : placesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="places-id">ID</Label>
                  <AvInput id="places-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="placeOfBirthLabel" for="places-placeOfBirth">
                  Place Of Birth
                </Label>
                <AvField
                  id="places-placeOfBirth"
                  data-cy="placeOfBirth"
                  type="text"
                  name="placeOfBirth"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 50, errorMessage: 'This field cannot be longer than 50 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="placeOfDeathLabel" for="places-placeOfDeath">
                  Place Of Death
                </Label>
                <AvField
                  id="places-placeOfDeath"
                  data-cy="placeOfDeath"
                  type="text"
                  name="placeOfDeath"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 50, errorMessage: 'This field cannot be longer than 50 characters.' },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/places" replace color="info">
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
  placesEntity: storeState.places.entity,
  loading: storeState.places.loading,
  updating: storeState.places.updating,
  updateSuccess: storeState.places.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PlacesUpdate);
