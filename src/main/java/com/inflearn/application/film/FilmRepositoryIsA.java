package com.inflearn.application.film;

import lombok.RequiredArgsConstructor;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JFilm;
import org.jooq.generated.tables.daos.FilmDao;
import org.springframework.stereotype.Repository;

@Repository
public class FilmRepositoryIsA extends FilmDao{
    private final DSLContext dslContext;

    public FilmRepositoryIsA(Configuration configuration, DSLContext dslContext) {
        super(configuration);
        this.dslContext = dslContext;
    }
}
