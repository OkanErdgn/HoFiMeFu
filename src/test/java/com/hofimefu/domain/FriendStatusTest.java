package com.hofimefu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hofimefu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FriendStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FriendStatus.class);
        FriendStatus friendStatus1 = new FriendStatus();
        friendStatus1.setId(1L);
        FriendStatus friendStatus2 = new FriendStatus();
        friendStatus2.setId(friendStatus1.getId());
        assertThat(friendStatus1).isEqualTo(friendStatus2);
        friendStatus2.setId(2L);
        assertThat(friendStatus1).isNotEqualTo(friendStatus2);
        friendStatus1.setId(null);
        assertThat(friendStatus1).isNotEqualTo(friendStatus2);
    }
}
