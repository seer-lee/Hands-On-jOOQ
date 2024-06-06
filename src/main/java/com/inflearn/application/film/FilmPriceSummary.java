package com.inflearn.application.film;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FilmPriceSummary {
    private Long filmId;
    private String title;
    private BigDecimal rentalRate;
    private PriceCategory priceCategory;
    private Long totalInventory;

    @Getter
    public enum PriceCategory {
        CHEAP("Cheap"),
        MODERATE("Moderate"),
        EXPENSIVE("Expensive");

        private final String code;

        PriceCategory(String code) {
            this.code = code;
        }
        public static PriceCategory findByCode(String code) {
            for (PriceCategory value : values()) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
            return null;
        }
    }
}
