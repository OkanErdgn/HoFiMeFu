package com.hofimefu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hofimefu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GlobalChatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalChat.class);
        GlobalChat globalChat1 = new GlobalChat();
        globalChat1.setId(1L);
        GlobalChat globalChat2 = new GlobalChat();
        globalChat2.setId(globalChat1.getId());
        assertThat(globalChat1).isEqualTo(globalChat2);
        globalChat2.setId(2L);
        assertThat(globalChat1).isNotEqualTo(globalChat2);
        globalChat1.setId(null);
        assertThat(globalChat1).isNotEqualTo(globalChat2);
    }
}
