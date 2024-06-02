package com.inflearn.application.film;

import lombok.RequiredArgsConstructor;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.JFilm;
import org.jooq.generated.tables.JFilmActor;
import org.jooq.generated.tables.daos.FilmDao;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilmRepositoryHasA {
    private final FilmDao filmDao;
    private final DSLContext dslContext;
    private final JFilm FILM = JFilm.FILM;

    public FilmRepositoryHasA(Configuration configuration, DSLContext dslContext) {
        this.filmDao = new FilmDao(configuration);
        this.dslContext = dslContext;
    }
    public List<Film> findByRangeBetween(Integer from, Integer to) {
        return filmDao.fetchByJLength(from, to);
    }
}
