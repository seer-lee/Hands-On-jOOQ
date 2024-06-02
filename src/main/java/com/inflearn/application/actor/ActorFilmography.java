package com.inflearn.application.actor;

import lombok.Getter;

@Getter
public class ActorFilmography {
    private final Actor actor;
    private final List<Film> filmList;

    public ActorFilmography(Actor actor, List<Film> filmList) {
        this.actor = actor;
        this.filmList = filmList;
    }
}
