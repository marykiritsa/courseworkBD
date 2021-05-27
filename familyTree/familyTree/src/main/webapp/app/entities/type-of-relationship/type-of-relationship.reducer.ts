import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITypeOfRelationship, defaultValue } from 'app/shared/model/type-of-relationship.model';

export const ACTION_TYPES = {
  FETCH_TYPEOFRELATIONSHIP_LIST: 'typeOfRelationship/FETCH_TYPEOFRELATIONSHIP_LIST',
  FETCH_TYPEOFRELATIONSHIP: 'typeOfRelationship/FETCH_TYPEOFRELATIONSHIP',
  CREATE_TYPEOFRELATIONSHIP: 'typeOfRelationship/CREATE_TYPEOFRELATIONSHIP',
  UPDATE_TYPEOFRELATIONSHIP: 'typeOfRelationship/UPDATE_TYPEOFRELATIONSHIP',
  PARTIAL_UPDATE_TYPEOFRELATIONSHIP: 'typeOfRelationship/PARTIAL_UPDATE_TYPEOFRELATIONSHIP',
  DELETE_TYPEOFRELATIONSHIP: 'typeOfRelationship/DELETE_TYPEOFRELATIONSHIP',
  RESET: 'typeOfRelationship/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITypeOfRelationship>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type TypeOfRelationshipState = Readonly<typeof initialState>;

// Reducer

export default (state: TypeOfRelationshipState = initialState, action): TypeOfRelationshipState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TYPEOFRELATIONSHIP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TYPEOFRELATIONSHIP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TYPEOFRELATIONSHIP):
    case REQUEST(ACTION_TYPES.UPDATE_TYPEOFRELATIONSHIP):
    case REQUEST(ACTION_TYPES.DELETE_TYPEOFRELATIONSHIP):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_TYPEOFRELATIONSHIP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_TYPEOFRELATIONSHIP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TYPEOFRELATIONSHIP):
    case FAILURE(ACTION_TYPES.CREATE_TYPEOFRELATIONSHIP):
    case FAILURE(ACTION_TYPES.UPDATE_TYPEOFRELATIONSHIP):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_TYPEOFRELATIONSHIP):
    case FAILURE(ACTION_TYPES.DELETE_TYPEOFRELATIONSHIP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TYPEOFRELATIONSHIP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_TYPEOFRELATIONSHIP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TYPEOFRELATIONSHIP):
    case SUCCESS(ACTION_TYPES.UPDATE_TYPEOFRELATIONSHIP):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_TYPEOFRELATIONSHIP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TYPEOFRELATIONSHIP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/type-of-relationships';

// Actions

export const getEntities: ICrudGetAllAction<ITypeOfRelationship> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TYPEOFRELATIONSHIP_LIST,
    payload: axios.get<ITypeOfRelationship>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ITypeOfRelationship> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TYPEOFRELATIONSHIP,
    payload: axios.get<ITypeOfRelationship>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITypeOfRelationship> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TYPEOFRELATIONSHIP,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITypeOfRelationship> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TYPEOFRELATIONSHIP,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ITypeOfRelationship> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_TYPEOFRELATIONSHIP,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITypeOfRelationship> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TYPEOFRELATIONSHIP,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
