package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HumanInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HumanInfo.class);
        HumanInfo humanInfo1 = new HumanInfo();
        humanInfo1.setId(1L);
        HumanInfo humanInfo2 = new HumanInfo();
        humanInfo2.setId(humanInfo1.getId());
        assertThat(humanInfo1).isEqualTo(humanInfo2);
        humanInfo2.setId(2L);
        assertThat(humanInfo1).isNotEqualTo(humanInfo2);
        humanInfo1.setId(null);
        assertThat(humanInfo1).isNotEqualTo(humanInfo2);
    }
}
