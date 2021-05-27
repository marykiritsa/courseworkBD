import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './relatives.reducer';
import { IRelatives } from 'app/shared/model/relatives.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRelativesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RelativesUpdate = (props: IRelativesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { relativesEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/relatives' + props.location.search);
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
        ...relativesEntity,
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
          <h2 id="familyTreeApp.relatives.home.createOrEditLabel" data-cy="RelativesCreateUpdateHeading">
            Create or edit a Relatives
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : relativesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="relatives-id">ID</Label>
                  <AvInput id="relatives-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="surnameLabel" for="relatives-surname">
                  Surname
                </Label>
                <AvField
                  id="relatives-surname"
                  data-cy="surname"
                  type="text"
                  name="surname"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 20, errorMessage: 'This field cannot be longer than 20 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="relatives-name">
                  Name
                </Label>
                <AvField
                  id="relatives-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 20, errorMessage: 'This field cannot be longer than 20 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="patronymicLabel" for="relatives-patronymic">
                  Patronymic
                </Label>
                <AvField
                  id="relatives-patronymic"
                  data-cy="patronymic"
                  type="text"
                  name="patronymic"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 20, errorMessage: 'This field cannot be longer than 20 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeOfRelationshipLabel" for="relatives-typeOfRelationship">
                  Type Of Relationship
                </Label>
                <AvField
                  id="relatives-typeOfRelationship"
                  data-cy="typeOfRelationship"
                  type="string"
                  className="form-control"
                  name="typeOfRelationship"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    min: { value: 0, errorMessage: 'This field should be at least 0.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/relatives" replace color="info">
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
  relativesEntity: storeState.relatives.entity,
  loading: storeState.relatives.loading,
  updating: storeState.relatives.updating,
  updateSuccess: storeState.relatives.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RelativesUpdate);
