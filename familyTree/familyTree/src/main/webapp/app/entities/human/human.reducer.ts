import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHuman, defaultValue } from 'app/shared/model/human.model';

export const ACTION_TYPES = {
  FETCH_HUMAN_LIST: 'human/FETCH_HUMAN_LIST',
  FETCH_HUMAN: 'human/FETCH_HUMAN',
  CREATE_HUMAN: 'human/CREATE_HUMAN',
  UPDATE_HUMAN: 'human/UPDATE_HUMAN',
  PARTIAL_UPDATE_HUMAN: 'human/PARTIAL_UPDATE_HUMAN',
  DELETE_HUMAN: 'human/DELETE_HUMAN',
  RESET: 'human/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHuman>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type HumanState = Readonly<typeof initialState>;

// Reducer

export default (state: HumanState = initialState, action): HumanState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_HUMAN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HUMAN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_HUMAN):
    case REQUEST(ACTION_TYPES.UPDATE_HUMAN):
    case REQUEST(ACTION_TYPES.DELETE_HUMAN):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_HUMAN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_HUMAN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HUMAN):
    case FAILURE(ACTION_TYPES.CREATE_HUMAN):
    case FAILURE(ACTION_TYPES.UPDATE_HUMAN):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_HUMAN):
    case FAILURE(ACTION_TYPES.DELETE_HUMAN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_HUMAN_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_HUMAN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_HUMAN):
    case SUCCESS(ACTION_TYPES.UPDATE_HUMAN):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_HUMAN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_HUMAN):
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

const apiUrl = 'api/humans';

// Actions

export const getEntities: ICrudGetAllAction<IHuman> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HUMAN_LIST,
    payload: axios.get<IHuman>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IHuman> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HUMAN,
    payload: axios.get<IHuman>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IHuman> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HUMAN,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHuman> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HUMAN,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IHuman> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_HUMAN,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHuman> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HUMAN,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
