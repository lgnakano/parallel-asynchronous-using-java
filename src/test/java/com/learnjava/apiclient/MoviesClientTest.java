package com.learnjava.apiclient;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

class MoviesClientTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    MoviesClient moviesClient = new MoviesClient(webClient);

    @Test
    void retrieveMovie() {

        // given
        var movieInfoId = 1L;

        //when

        var movie = moviesClient.retrieveMovie(movieInfoId);

        System.out.println("movie: " + movie);

        //then
        assert movie != null;
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assert movie.getReviewList().size() == 1;
    }
}
