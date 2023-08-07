package com.example.jariBean.service;

import com.example.jariBean.entity.Cafe;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ManagerServiceTest {

    @Test
    public void toggleMatchingStatusTest() throws Exception {
        // given
        Cafe cafe = Cafe.builder().build();
        boolean beforeStatus = cafe.isMatching();

        // when
        boolean afterStatus = cafe.toggleMatchingStatus();

        // then
        assertThat(beforeStatus).isNotEqualTo(afterStatus);
    }

}