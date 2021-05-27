import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRelatives, defaultValue } from 'app/shared/model/relatives.model';

export const ACTION_TYPES = {
  FETCH_RELATIVES_LIST: 'relatives/FETCH_RELATIVES_LIST',
  FETCH_RELATIVES: 'relatives/FETCH_RELATIVES',
  CREATE_RELATIVES: 'relatives/CREATE_RELATIVES',
  UPDATE_RELATIVES: 'relatives/UPDATE_RELATIVES',
  PARTIAL_UPDATE_RELATIVES: 'relatives/PARTIAL_UPDATE_RELATIVES',
  DELETE_RELATIVES: 'relatives/DELETE_RELATIVES',
  RESET: 'relatives/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRelatives>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type RelativesState = Readonly<typeof initialState>;

// Reducer

export default (state: RelativesState = initialState, action): RelativesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RELATIVES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RELATIVES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_RELATIVES):
    case REQUEST(ACTION_TYPES.UPDATE_RELATIVES):
    case REQUEST(ACTION_TYPES.DELETE_RELATIVES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_RELATIVES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_RELATIVES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RELATIVES):
    case FAILURE(ACTION_TYPES.CREATE_RELATIVES):
    case FAILURE(ACTION_TYPES.UPDATE_RELATIVES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_RELATIVES):
    case FAILURE(ACTION_TYPES.DELETE_RELATIVES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_RELATIVES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_RELATIVES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_RELATIVES):
    case SUCCESS(ACTION_TYPES.UPDATE_RELATIVES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_RELATIVES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_RELATIVES):
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

const apiUrl = 'api/relatives';

// Actions

export const getEntities: ICrudGetAllAction<IRelatives> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RELATIVES_LIST,
    payload: axios.get<IRelatives>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IRelatives> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RELATIVES,
    payload: axios.get<IRelatives>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRelatives> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RELATIVES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRelatives> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RELATIVES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IRelatives> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_RELATIVES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRelatives> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RELATIVES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
