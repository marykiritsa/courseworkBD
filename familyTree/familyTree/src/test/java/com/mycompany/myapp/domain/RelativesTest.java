package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RelativesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Relatives.class);
        Relatives relatives1 = new Relatives();
        relatives1.setId(1L);
        Relatives relatives2 = new Relatives();
        relatives2.setId(relatives1.getId());
        assertThat(relatives1).isEqualTo(relatives2);
        relatives2.setId(2L);
        assertThat(relatives1).isNotEqualTo(relatives2);
        relatives1.setId(null);
        assertThat(relatives1).isNotEqualTo(relatives2);
    }
}
