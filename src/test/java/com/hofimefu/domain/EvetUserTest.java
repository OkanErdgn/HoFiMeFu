package com.hofimefu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hofimefu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvetUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvetUser.class);
        EvetUser evetUser1 = new EvetUser();
        evetUser1.setId(1L);
        EvetUser evetUser2 = new EvetUser();
        evetUser2.setId(evetUser1.getId());
        assertThat(evetUser1).isEqualTo(evetUser2);
        evetUser2.setId(2L);
        assertThat(evetUser1).isNotEqualTo(evetUser2);
        evetUser1.setId(null);
        assertThat(evetUser1).isNotEqualTo(evetUser2);
    }
}
