package com.inflearn.application.film;

import com.inflearn.application.web.FilmWithActorPagedResponse;
import com.inflearn.application.web.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    public FilmWithActorPagedResponse getFilmActorPageResponse(Long page, Long pageSize) {
        List<FilmWithActors> filmWithActorsList = filmRepository.findFilmWithActorsList(page, pageSize);
        PagedResponse pagedResponse = new PagedResponse(page, pageSize);
        return new FilmWithActorPagedResponse(pagedResponse, filmWithActorsList);
    }
}
