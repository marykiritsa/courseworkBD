package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfRelationshipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfRelationship.class);
        TypeOfRelationship typeOfRelationship1 = new TypeOfRelationship();
        typeOfRelationship1.setId(1L);
        TypeOfRelationship typeOfRelationship2 = new TypeOfRelationship();
        typeOfRelationship2.setId(typeOfRelationship1.getId());
        assertThat(typeOfRelationship1).isEqualTo(typeOfRelationship2);
        typeOfRelationship2.setId(2L);
        assertThat(typeOfRelationship1).isNotEqualTo(typeOfRelationship2);
        typeOfRelationship1.setId(null);
        assertThat(typeOfRelationship1).isNotEqualTo(typeOfRelationship2);
    }
}
