package com.inflearn.application;

import com.inflearn.application.actor.ActorFilmography;
import com.inflearn.application.actor.ActorFilmographySearchOption;
import com.inflearn.application.actor.ActorRepository;
import org.assertj.core.api.Assertions;
import org.jooq.generated.tables.pojos.Actor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
public class JOOQConditionTest {
    @Autowired
    private ActorRepository actorRepository;
    @Test
    @DisplayName("and 조건 검색 - firstName과 LastName이 같은 사람 찾기")
    void AND조건_1() {
        // given
        String first = "ED";
        String last = "CHASE";
        // when
        List<Actor> actorList = actorRepository.findBtyFirstNameAndLastName(first, last);
        // then
        Assertions.assertThat(actorList).hasSize(1);
    }
    @DisplayName("or 조건 검색 - firstName이나 LastName이 같은 사람 찾기")
    @Test
    void OR조건_1() {
        // given
        String first = "ED";
        String last = "CHASE";
        // when
        List<Actor> actorList = actorRepository.findByFirstNameOrLastName(first, last);
        // then
        Assertions.assertThat(actorList).hasSizeGreaterThan(1);
    }
    @DisplayName("in절 - 동적 조건 검색")
    @Test
    void in절_동적_조건검색_1() {
        // given //when
        List.of(1L);
        List<Actor> actorList = actorRepository.findByActorIdIn(null);

        // then
        Assertions.assertThat(actorList).hasSizeGreaterThan(1);
    }
    @DisplayName("in절 - 동적 조건 검색 - empty list시 제외")
    @Test
    void in절_동적_조건검색_2() {
        // given // 조두
        List<Actor> actorList = actorRepository.findByActorIdIn(Collections.EMPTY_LIST);

        // then
        Assertions.assertThat(actorList).isEmpty()
    }
    @DisplayName("다중 조건 검색 - 배우 이름으로 조회")
    @Test
    void 다중_조건_검색_1() {
        // given
        var searchOption = ActorFilmographySearchOption.builder()
                .actorName("LOLLOBRIGIDA")
                .build();

        // when
        List<ActorFilmography> actorFilmographies = actorRepository.findActorFilmography(searchOption);

        // then
        Assertions.assertThat(actorFilmographies).hasSize(1);
    }
}
