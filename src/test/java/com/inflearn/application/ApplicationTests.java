package com.inflearn.application;

import com.inflearn.application.film.FilmRepository;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {
	@Autowired
	DSLContext dslContext;
	@Autowired
	FilmRepository filmRepository;

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

}
