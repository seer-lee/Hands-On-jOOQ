package com.inflearn.application.web;

import com.inflearn.application.film.FilmWithActors;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class FilmWithActorPagedResponse {
    private final PagedResponse page;
    private final List<FilmActorResponse> filmActorList;

    public FilmWithActorPagedResponse(PagedResponse page, List<FilmWithActors> filmActorList) {
        this.page = page;
        this.filmActorList = filmActorList.stream()
                .map(FilmActorResponse::new)
                .toList();
    }

    @Getter
    public static class FilmActorResponse {
        private final String filmTitle;
        private final int filmLength;
        private final String actorFullName;

        public FilmActorResponse(FilmWithActors filmWithActors) {
            this.filmTitle = filmWithActors.getFilm().getTitle();
            this.filmLength = filmWithActors.getFilm().getLength();
            this.actorFullName = filmWithActors.getFullActorName();
        }
    }

}
