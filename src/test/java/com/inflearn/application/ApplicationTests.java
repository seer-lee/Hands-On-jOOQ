package com.inflearn.application;

import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
	@Autowired
	DSLContext dslContext;

	@Test
	void contextLoads() {
		dslContext.selectFrom(JActor.ACTOR)
				.limit(10)
				.fetch();
	}

}
