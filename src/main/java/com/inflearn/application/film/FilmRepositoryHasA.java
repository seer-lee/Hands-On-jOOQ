package com.inflearn.application.film;

import com.inflearn.application.config.converter.PriceCategoryConverter;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.generated.tables.JFilm;
import org.jooq.generated.tables.JInventory;
import org.jooq.generated.tables.JRental;
import org.jooq.generated.tables.daos.FilmDao;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.inflearn.application.utils.jooq.JooqListConditionUtil.containsIfNotBlank;
import static org.jooq.impl.DSL.*;

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

    public List<FilmPriceSummary> findFilmPriceSummaryByFilmTitle(String filmTitle) {
        final JInventory INVENTORY = JInventory.INVENTORY;
        return dslContext.select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        FILM.RENTAL_RATE,
                        DSL.case_()
                                .when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(1.0)), "Cheap")
                                .when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(3.0)), "Moderate")
                                .else_("Expensive").as("price_category").convert(new PriceCategoryConverter()),
                        selectCount().from(INVENTORY).where(INVENTORY.FILM_ID.eq(FILM.FILM_ID)).asField("total_inventory")
                ).from(FILM)
                .where(containsIfNotBlank(FILM.TITLE, filmTitle))
                .fetchInto(FilmPriceSummary.class);
    }

    public List<FilmRentalSummary> findFilmRentalSummaryByFilmTitle(String filmTitle) {
        JInventory INVENTORY = JInventory.INVENTORY;
        JRental RENTAL = JRental.RENTAL;

        var rentalDurationInfoSubQuery = select(
                INVENTORY.FILM_ID,
                DSL.avg(DSL.localDateTimeDiff(DatePart.DAY, RENTAL.RENTAL_DATE, RENTAL.RETURN_DATE)).as("average_rental_duration"))
                .from(RENTAL)
                .join(INVENTORY)
                .on(RENTAL.INVENTORY_ID.eq(INVENTORY.INVENTORY_ID))
                .where(RENTAL.RENTAL_DATE.isNotNull())
                .groupBy(INVENTORY.FILM_ID)
                .asTable("rental_duration_info");


        return dslContext.select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        rentalDurationInfoSubQuery.field("average_rental_duration")
                ).from(FILM)
                .join(rentalDurationInfoSubQuery)
                .on(FILM.FILM_ID.eq(rentalDurationInfoSubQuery.field(INVENTORY.FILM_ID)))
                .where(containsIfNotBlank(FILM.TITLE, filmTitle))
                .orderBy(field(name("average_rental_duration")).desc())
                .fetchInto(FilmRentalSummary.class);
    }

    public List<Film> findRentedFilmByTitle(String filmTitle) {
        JInventory INVENTORY = JInventory.INVENTORY;
        JRental RENTAL = JRental.RENTAL;

        return dslContext.selectFrom(FILM)
                .whereExists(
                        DSL.selectOne().from(INVENTORY)
                                .join(RENTAL)
                                .on(RENTAL.INVENTORY_ID.eq(INVENTORY.INVENTORY_ID))
                                .where(INVENTORY.FILM_ID.eq(FILM.FILM_ID))
                ).and(containsIfNotBlank(FILM.TITLE, filmTitle))
                .fetchInto(Film.class);
    }
}
