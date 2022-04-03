package com.learnjava.apiclient;

import com.learnjava.util.CommonUtil;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static com.learnjava.util.CommonUtil.stopWatchReset;
import static org.junit.jupiter.api.Assertions.*;

class MoviesClientTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    MoviesClient moviesClient = new MoviesClient(webClient);

    @RepeatedTest(10)
    void retrieveMovie() {
        CommonUtil.startTimer();
        // given
        var movieInfoId = 1L;

        //when

        var movie = moviesClient.retrieveMovie(movieInfoId);
        CommonUtil.timeTaken();
        CommonUtil.stopWatchReset();

        System.out.println("movie: " + movie);

        //then
        assert movie != null;
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assert movie.getReviewList().size() == 1;

    }

    @RepeatedTest(10)
    void retrieveMovie_CF() {

        CommonUtil.startTimer();
        // given
        var movieInfoId = 1L;

        //when

        var movie = moviesClient.retrieveMovie_CF(movieInfoId)
                .join();
        CommonUtil.timeTaken();
        CommonUtil.stopWatchReset();

        System.out.println("movie: " + movie);

        //then
        assert movie != null;
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assert movie.getReviewList().size() == 1;
    }
}
