package com.sandborn.feedback.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sandborn.feedback.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DesignationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Designation.class);
        Designation designation1 = new Designation();
        designation1.setId(1L);
        Designation designation2 = new Designation();
        designation2.setId(designation1.getId());
        assertThat(designation1).isEqualTo(designation2);
        designation2.setId(2L);
        assertThat(designation1).isNotEqualTo(designation2);
        designation1.setId(null);
        assertThat(designation1).isNotEqualTo(designation2);
    }
}
