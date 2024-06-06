package com.inflearn.application;

import com.inflearn.application.actor.ActorRepository;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.records.ActorRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class jooqActiveRecordTest {
    @Autowired
    ActorRepository actorRepository;

    @Autowired
    DSLContext dslContext;

    @DisplayName("SELECT 절 예제")
    @Test
    void activeRecord_조회_예제() {
        // given
        Long actorId = 1L;

        // when
        ActorRecord actorRecord = actorRepository.findRecordByActorId(actorId);

        // then
        assertThat(actorRecord).hasNoNullFieldsOrProperties();
    }

    @DisplayName("activeRecord refresh 예제")
    @Test
    void activeRecord_refresh_예제() {
        // given
        Long actorId = 1L;
        ActorRecord actorRecord = actorRepository.findRecordByActorId(actorId);
        actorRecord.setFirstName(null);
        // when
        actorRecord.refresh();

        // then
        assertThat(actorRecord.getFirstName()).isNotNull();
    }

    @DisplayName("activeRecord store 예제 - insert")
    @Test
    @Transactional
    void activeRecord_insert_예제() {
        // given
        ActorRecord actorRecord = dslContext.newRecord(JActor.ACTOR);

        // when
        actorRecord.setFirstName("john");
        actorRecord.setLastName("doe");
        actorRecord.store();
        actorRecord.refresh();

        // then
        assertThat(actorRecord.getLastUpdate()).isNotNull();
    }

    @DisplayName("activeRecord store 예제 - update")
    @Test
    @Transactional
    void activeRecord_update_예제() {
        // given
        Long actorId = 1L;
        String newName = "test";
        ActorRecord actor = actorRepository.findRecordByActorId(actorId);

        // when
        actor.setFirstName(newName);
        actor.store();

        // then
        assertThat(actor.getFirstName()).isEqualTo(newName);
    }

    @DisplayName("activeRecord delete 예제 - delete")
    @Test
    @Transactional
    void activeRecord_delete_예제() {
        // given
        ActorRecord actorRecord = dslContext.newRecord(JActor.ACTOR);

        actorRecord.setFirstName("john");
        actorRecord.setLastName("doe");
        actorRecord.store();

        // when
        int result = actorRecord.delete();

        // then
        assertThat(result).isEqualTo(1);
    }
}
