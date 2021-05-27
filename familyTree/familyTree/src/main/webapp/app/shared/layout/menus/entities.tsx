import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/human">
      Human
    </MenuItem>
    <MenuItem icon="asterisk" to="/human-info">
      Human Info
    </MenuItem>
    <MenuItem icon="asterisk" to="/relatives">
      Relatives
    </MenuItem>
    <MenuItem icon="asterisk" to="/type-of-relationship">
      Type Of Relationship
    </MenuItem>
    <MenuItem icon="asterisk" to="/places">
      Places
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
