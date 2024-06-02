package com.inflearn.application;

import com.inflearn.application.film.FilmRepositoryHasA;
import com.inflearn.application.film.FilmRepositoryIsA;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class jOOQDaoWrapperTest {
    @Autowired
    FilmRepositoryHasA filmRepositoryHasA; // 컴포지트
    @Autowired
    FilmRepositoryIsA filmRepositoryIsA;

    @Test
    void test() {
        Film byId = filmRepositoryIsA.findById(10L);
    }
    @Test
    @DisplayName(""" 
            상속) 자동생성 DAO 사용
               - 영화 길이가 100 ~ 180 분 사이인 영화 조회
            """)
    void 상속_DAO_1() {
        // given
        var start = 100;
        var end = 180;

        // when
        List<Film> films = filmRepositoryIsA.fetchRangeOfJLength(start, end);

        // then
        assertThat(films).allSatisfy(film ->
                assertThat(film.getLength()).isBetween(start, end)
        );
    }

    @Test
    @DisplayName(""" 
            컴포지션) 자동생성 DAO 사용
               - 영화 길이가 100 ~ 180 분 사이인 영화 조회
            """)
    void 컴포지션_DAO_1() {
        // given
        var start = 100;
        var end = 180;

        // when
        List<Film> films = filmRepositoryHasA.findByRangeBetween(start, end);

        // then
        assertThat(films).allSatisfy(film ->
                assertThat(film.getLength()).isBetween(start, end)
        );
    }
}
