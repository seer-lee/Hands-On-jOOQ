package com.inflearn.application.utils.jooq;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class JooqListConditionUtil {
    public static <T> Condition inIfNotEmpty(Field<T> actorId, List<T> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return DSL.noCondition();
        }
        return actorId.in(idList);
    }
    public static Condition containsIfNotBlank(Field<String> field, String value) {
        if (value == null || value.isEmpty()) {
            return DSL.noCondition();
        }
        return field.like("%"+value"%");
    }
}
