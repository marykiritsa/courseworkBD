import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPlaces, defaultValue } from 'app/shared/model/places.model';

export const ACTION_TYPES = {
  FETCH_PLACES_LIST: 'places/FETCH_PLACES_LIST',
  FETCH_PLACES: 'places/FETCH_PLACES',
  CREATE_PLACES: 'places/CREATE_PLACES',
  UPDATE_PLACES: 'places/UPDATE_PLACES',
  PARTIAL_UPDATE_PLACES: 'places/PARTIAL_UPDATE_PLACES',
  DELETE_PLACES: 'places/DELETE_PLACES',
  RESET: 'places/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPlaces>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type PlacesState = Readonly<typeof initialState>;

// Reducer

export default (state: PlacesState = initialState, action): PlacesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PLACES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PLACES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PLACES):
    case REQUEST(ACTION_TYPES.UPDATE_PLACES):
    case REQUEST(ACTION_TYPES.DELETE_PLACES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PLACES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PLACES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PLACES):
    case FAILURE(ACTION_TYPES.CREATE_PLACES):
    case FAILURE(ACTION_TYPES.UPDATE_PLACES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PLACES):
    case FAILURE(ACTION_TYPES.DELETE_PLACES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PLACES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PLACES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PLACES):
    case SUCCESS(ACTION_TYPES.UPDATE_PLACES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PLACES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PLACES):
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

const apiUrl = 'api/places';

// Actions

export const getEntities: ICrudGetAllAction<IPlaces> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PLACES_LIST,
    payload: axios.get<IPlaces>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IPlaces> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PLACES,
    payload: axios.get<IPlaces>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPlaces> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PLACES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPlaces> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PLACES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IPlaces> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PLACES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPlaces> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PLACES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
