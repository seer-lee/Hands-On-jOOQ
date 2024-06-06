package com.inflearn.application.config.converter;

import com.inflearn.application.film.FilmPriceSummary;
import org.jooq.impl.EnumConverter;

public class PriceCategoryConverter extends EnumConverter<String, FilmPriceSummary.PriceCategory> {
    public PriceCategoryConverter() {
        super(String.class,
                FilmPriceSummary.PriceCategory.class,
                FilmPriceSummary.PriceCategory::getCode);
    }
}
