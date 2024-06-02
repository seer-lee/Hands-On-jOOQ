package com.inflearn.application;

import com.inflearn.application.film.FilmRepository;
import com.inflearn.application.film.FilmService;
import com.inflearn.application.film.FilmWithActors;
import com.inflearn.application.film.SimpleFilmInfo;
import com.inflearn.application.web.FilmWithActorPagedResponse;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
class ApplicationTests {
	@Autowired
	DSLContext dslContext;
	@Autowired
	FilmRepository filmRepository;

	@Autowired
	FilmService filmService;

	@Test
	void contextLoads() {
		dslContext.selectFrom(JActor.ACTOR)
				.limit(10)
				.fetch();
	}
	@Test
	@DisplayName("영화 정보를 조회 한다.")
	void getFilm() {
		Film film = filmRepository.findById(1L);
		assertThat(film).isNotNull();
	}
	@Test
	@DisplayName("영화 정보 간략 조회")
	void getFilmBySimpleFilmInfo() {
		SimpleFilmInfo simpleFilmInfo = filmRepository.findByAsSimpleFilmInfoById(1L);
		assertThat(simpleFilmInfo).hasNoNullFieldsOrProperties();
	}
	@Test
	@DisplayName("영화와 배우 정보를 페이징하여 조회")
	void test() {
		FilmWithActorPagedResponse filmWithActorPagedResponse = filmService.getFilmActorPageResponse(1L,20L);
		assertThat(filmWithActorPagedResponse.getFilmActorList()).hasSize(20);
	}

}
