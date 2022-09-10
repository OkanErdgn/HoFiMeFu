package com.hofimefu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hofimefu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FriendTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Friend.class);
        Friend friend1 = new Friend();
        friend1.setId(1L);
        Friend friend2 = new Friend();
        friend2.setId(friend1.getId());
        assertThat(friend1).isEqualTo(friend2);
        friend2.setId(2L);
        assertThat(friend1).isNotEqualTo(friend2);
        friend1.setId(null);
        assertThat(friend1).isNotEqualTo(friend2);
    }
}
