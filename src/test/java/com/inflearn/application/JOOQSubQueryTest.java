package com.inflearn.application;

import com.inflearn.application.film.FilmPriceSummary;
import com.inflearn.application.film.FilmRentalSummary;
import com.inflearn.application.film.FilmRepository;
import com.inflearn.application.film.FilmRepositoryHasA;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JOOQSubQueryTest {
    @Autowired
    private FilmRepositoryHasA filmRepositoryHasA;
    @DisplayName("""
            영화별 대여료가
            1.0 이하면 Cheap
            3.0 이하면 Moderate
            그 이상이면 Expensive 로 분류하고
            각 영화의 총 재고 수를 조회한다.
            """)
    @Test
    void 스칼라_서브쿼리_예제() {
        // given
        String filmTitle = "EGG";
        // when
        List<FilmPriceSummary> filmPriceSummaryList = filmRepositoryHasA.findFilmPriceSummaryByFilmTitle(filmTitle);
        // then
        assertThat(filmPriceSummaryList).isNotEmpty();
    }
    @DisplayName("평균 대여 기간이 가장 긴 영화부터 정렬해서 조회한다.")
    @Test
    void from절_서브쿼리_인라인뷰_예제() {
        // given
        String filmTitle = "EGG";
        // when
        List<FilmRentalSummary> rentalSummaryList = filmRepositoryHasA.findFilmRentalSummaryByFilmTitle(filmTitle);

        // then
        assertThat(rentalSummaryList).isNotEmpty();
    }
    @DisplayName("대여된 기록이 있는 영화가 있는 영화만 조회")
    @Test
    void 조건절_서브쿼리_예제() {
        // given
        String filmTitle = "EGG";
        // when
        List<Film> filmList = filmRepositoryHasA.findRentedFilmByTitle(filmTitle);

        // then
        assertThat(filmList).isNotEmpty();
    }
}
