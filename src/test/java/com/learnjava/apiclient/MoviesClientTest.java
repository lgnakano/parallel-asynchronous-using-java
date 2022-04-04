package com.learnjava.apiclient;

import com.learnjava.util.CommonUtil;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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

    @RepeatedTest(10)
    void retrieveMovies() {
        CommonUtil.startTimer();
        // given
        var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        //when

        var movies = moviesClient.retrieveMovies(movieInfoIds);
        CommonUtil.timeTaken();
        CommonUtil.stopWatchReset();

        System.out.println("movie: " + movies);

        //then
        assert movies != null;
        assert movies.size() == 7;
        assertEquals("Batman Begins", movies.get(0).getMovieInfo().getName());
        assert movies.get(0).getReviewList().size() == 1;

    }

    @RepeatedTest(10)
    void retrieveMovies_CF() {

        CommonUtil.startTimer();
        // given
        var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        //when

        var movies = moviesClient.retrieveMovies_CF(movieInfoIds);
        CommonUtil.timeTaken();
        CommonUtil.stopWatchReset();

        System.out.println("movie: " + movies);

        //then
        assert movies != null;
        assert movies.size() == 7;
        assertEquals("Batman Begins", movies.get(0).getMovieInfo().getName());
        assert movies.get(0).getReviewList().size() == 1;
    }
    @RepeatedTest(10)
    void retrieveMovies_CF_single_stream() {

        CommonUtil.startTimer();
        // given
        var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        //when

        var movies = moviesClient.retrieveMovies_CF_single_stream(movieInfoIds);
        CommonUtil.timeTaken();
        CommonUtil.stopWatchReset();

        System.out.println("movie: " + movies);

        //then
        assert movies != null;
        assert movies.size() == 7;
        assertEquals("Batman Begins", movies.get(0).getMovieInfo().getName());
        assert movies.get(0).getReviewList().size() == 1;
    }
    @RepeatedTest(10)
    void retrieveMovies_CF_allOf() {

        CommonUtil.startTimer();
        // given
        var movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

        //when

        var movies = moviesClient.retrieveMovies_CF_allOf(movieInfoIds);
        CommonUtil.timeTaken();
        CommonUtil.stopWatchReset();

        System.out.println("movie: " + movies);

        //then
        assert movies != null;
        assert movies.size() == 7;
        assertEquals("Batman Begins", movies.get(0).getMovieInfo().getName());
        assert movies.get(0).getReviewList().size() == 1;
    }
}
