package com.inflearn.application.actor;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.JFilm;
import org.jooq.generated.tables.JFilmActor;
import org.jooq.generated.tables.daos.ActorDao;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.generated.tables.records.ActorRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.inflearn.application.utils.jooq.JooqListConditionUtil.containsIfNotBlank;
import static com.inflearn.application.utils.jooq.JooqListConditionUtil.inIfNotEmpty;
import static org.jooq.impl.DSL.noField;
import static org.jooq.impl.DSL.val;

@Repository
public class ActorRepository {

    private final DSLContext dslContext;
    private final ActorDao actorDao;
    private final JActor ACTOR = JActor.ACTOR;

    public ActorRepository(DSLContext dslContext, Configuration configuration) {
        this.dslContext = dslContext;
        this.actorDao = new ActorDao(configuration);

    }

    public List<Actor> findBtyFirstNameAndLastName(String firstName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(ACTOR.FIRST_NAME.eq(firstName).and(ACTOR.LAST_NAME.eq(lastName)))
                .fetchInto(Actor.class);
    }

    public List<Actor> findByFirstNameOrLastName(String firstName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(ACTOR.FIRST_NAME.eq(firstName).or(ACTOR.LAST_NAME.eq(lastName)))
                .fetchInto(Actor.class);
    }

    public List<Actor> findByActorIdIn(List<Long> idList) {
        return dslContext.selectFrom(ACTOR)
                .where(inIfNotEmpty(ACTOR.ACTOR_ID, idList))
                .fetchInto(Actor.class);
    }

    public List<ActorFilmography> findActorFilmography(ActorFilmographySearchOption searchOption) {
        final JFilmActor FILM_ACTOR = JFilmActor.FILM_ACTOR;
        final JFilm FILM = JFilm.FILM;

        Map<Actor, List<Film>> actorListMap = dslContext.select(
                        DSL.row(ACTOR.fields()).as("actor"),
                        DSL.row(FILM.fields()).as("film")
                ).from(ACTOR)
                .join(FILM_ACTOR).on(ACTOR.ACTOR_ID.eq(FILM_ACTOR.ACTOR_ID))
                .join(FILM).on(FILM_ACTOR.FILM_ID.eq(FILM.FILM_ID))
                .where(
                        // 배우 full name like 검색
                        containsIfNotBlank(ACTOR.FIRST_NAME.concat(" ").concat(ACTOR.LAST_NAME), searchOption.getActorName()),

                        // 영화 제목 like 검색
                        containsIfNotBlank(FILM.TITLE, searchOption.getFilmTitle())
                )
                .fetchGroups(
                        record -> record.get("actor", Actor.class),
                        record -> record.get("film", Film.class)
                );
        return actorListMap.entrySet().stream()
                .map(entry -> new ActorFilmography(entry.getKey(), entry.getValue()))
                .toList();
    }

    public Actor saveByDao(Actor actor) {
        actorDao.insert(actor);
        return actor;
    }

    public ActorRecord saveByRecord(Actor actor) {
        ActorRecord actorRecord = dslContext.newRecord(ACTOR, actor);
        actorRecord.insert();
        return actorRecord;
    }

    public Long saveWithReturningPkOnly(Actor actor) {
        return dslContext
                .insertInto(ACTOR, ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                .values(actor.getFirstName(), actor.getLastName())
                .returningResult(ACTOR.ACTOR_ID)
                .fetchOneInto(Long.class);
    }

    public Actor saveWithReturning(Actor actor) {
        return dslContext
                .insertInto(ACTOR, ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                .values(actor.getFirstName(), actor.getLastName())
                .returning(ACTOR.fields())
                .fetchOneInto(Actor.class);
    }

    public void bulkInsertWithRows(List<Actor> actorList) {
        var rows = actorList.stream()
                .map(actor -> DSL.row(actor.getFirstName(), actor.getLastName()))
                .toList();
        dslContext
                .insertInto(ACTOR, ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                .valuesOfRows(rows)
                .executeAsync();
    }

    public void update(Actor actor) {
        actorDao.update(actor);
    }

    public Actor findByActorId(Long actorId) {
        return actorDao.findById(actorId);
    }

    public int updateWithDto(Long newActorId, ActorUpdateRequest request) {
        var firstName = StringUtils
                .hasText(request.getFirstName()) ? val(request.getFirstName()) : noField(ACTOR.FIRST_NAME);
        var lastName = StringUtils
                .hasText(request.getLastName()) ? val(request.getLastName()) : noField(ACTOR.LAST_NAME);
        return dslContext.update(ACTOR)
                .set(ACTOR.FIRST_NAME, firstName)
                .set(ACTOR.LAST_NAME, lastName)
                .where(ACTOR.ACTOR_ID.eq(newActorId))
                .execute();
    }

    public int updateWithRecord(Long newActorId, ActorUpdateRequest request) {
        var record = dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(newActorId));

        if (StringUtils.hasText(request.getFirstName())) {
            record.setFirstName(request.getFirstName());
        }
        if (StringUtils.hasText(request.getLastName())) {
            record.setLastName(request.getLastName());
        }
        return dslContext.update(ACTOR)
                .set(record)
                .where(ACTOR.ACTOR_ID.eq(newActorId))
                .execute();
    }

    public int delete(Long newActorId) {
        return dslContext.deleteFrom(ACTOR)
                .where(ACTOR.ACTOR_ID.eq(newActorId))
                .execute();
    }

    public int deleteWithRecord(Long newActorId) {
        ActorRecord actorRecord = dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(newActorId));
        return actorRecord.delete();
    }

    public ActorRecord findRecordByActorId(Long actorId) {
        return dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(actorId));
    }
}