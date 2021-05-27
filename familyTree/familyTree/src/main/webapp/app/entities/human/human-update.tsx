import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './human.reducer';
import { IHuman } from 'app/shared/model/human.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHumanUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HumanUpdate = (props: IHumanUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { humanEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/human' + props.location.search);
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
        ...humanEntity,
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
          <h2 id="familyTreeApp.human.home.createOrEditLabel" data-cy="HumanCreateUpdateHeading">
            Create or edit a Human
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : humanEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="human-id">ID</Label>
                  <AvInput id="human-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="surnameLabel" for="human-surname">
                  Surname
                </Label>
                <AvField
                  id="human-surname"
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
                <Label id="nameLabel" for="human-name">
                  Name
                </Label>
                <AvField
                  id="human-name"
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
                <Label id="patronymicLabel" for="human-patronymic">
                  Patronymic
                </Label>
                <AvField
                  id="human-patronymic"
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
                <Label id="humanInfoLabel" for="human-humanInfo">
                  Human Info
                </Label>
                <AvField
                  id="human-humanInfo"
                  data-cy="humanInfo"
                  type="string"
                  className="form-control"
                  name="humanInfo"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    min: { value: 0, errorMessage: 'This field should be at least 0.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/human" replace color="info">
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
  humanEntity: storeState.human.entity,
  loading: storeState.human.loading,
  updating: storeState.human.updating,
  updateSuccess: storeState.human.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HumanUpdate);
