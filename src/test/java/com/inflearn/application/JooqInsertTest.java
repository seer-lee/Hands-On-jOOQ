package com.inflearn.application;

import com.inflearn.application.actor.ActorRepository;
import org.assertj.core.api.Assertions;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.records.ActorRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class JooqInsertTest {
    @Autowired
    ActorRepository actorRepository;

    @DisplayName("자동생성된 DAO 를 통한 insert 테스트")
    @Test
    @Transactional
    void insert_dao() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());
        // when
        actorRepository.saveByDao(actor);
        // then
        Assertions.assertThat(actor.getActorId()).isNotNull();
    }
    @DisplayName("ActiveRecord 를 통한 insert 테스트")
    @Test
    @Transactional
    void insert_by_record() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        ActorRecord actorRecord = actorRepository.saveByRecord(actor);

        // then
        Assertions.assertThat(actorRecord.getActorId()).isNotNull();
        Assertions.assertThat(actor.getActorId()).isNull();
    }
    @DisplayName("SQL 실행 후 PK 만 반환")
    @Test
    @Transactional
    void insert_with_returning_pk() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        Long pk = actorRepository.saveWithReturningPkOnly(actor);

        // then
        Assertions.assertThat(pk).isNotNull();
    }
    @DisplayName("SQL 실행 후 해당 ROW 만 반환")
    @Test
    @Transactional
    void insert_with_returning() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");

        // when
        Actor newActor = actorRepository.saveWithReturning(actor);

        // then
        Assertions.assertThat(newActor).hasNoNullFieldsOrProperties();
    }
    @DisplayName("BULK INSERT 예제")
    @Test
    @Transactional
    void bulk_insert() {
        // given
        Actor actor1 = new Actor();
        actor1.setFirstName("John");
        actor1.setLastName("Doe");

        Actor actor2 = new Actor();
        actor2.setFirstName("John 2");
        actor2.setLastName("Doe2");

        // when
        List<Actor> actorList = List.of(actor1, actor1);
        actorRepository.bulkInsertWithRows(actorList);
        // then
    }

}
