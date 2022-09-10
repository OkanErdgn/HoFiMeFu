package com.hofimefu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hofimefu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSchoolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSchool.class);
        UserSchool userSchool1 = new UserSchool();
        userSchool1.setId(1L);
        UserSchool userSchool2 = new UserSchool();
        userSchool2.setId(userSchool1.getId());
        assertThat(userSchool1).isEqualTo(userSchool2);
        userSchool2.setId(2L);
        assertThat(userSchool1).isNotEqualTo(userSchool2);
        userSchool1.setId(null);
        assertThat(userSchool1).isNotEqualTo(userSchool2);
    }
}
