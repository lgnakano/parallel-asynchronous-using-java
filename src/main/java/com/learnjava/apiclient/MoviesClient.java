package com.learnjava.apiclient;

import com.learnjava.domain.movie.Movie;
import com.learnjava.domain.movie.MovieInfo;
import com.learnjava.domain.movie.Review;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MoviesClient {

    private final WebClient webClient;

    public MoviesClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Movie retrieveMovie(Long movieInfoId) {

        // movieInfo
        var movieInfo = invokeMovieInfoService(movieInfoId);

        // review
        var reviews = invokeReviewsService(movieInfoId);

        return new Movie(movieInfo, reviews);
    }

    public List<Movie> retrieveMovies(List<Long> movieInfoIds) {
        return movieInfoIds
                .stream()
                .map(this::retrieveMovie)
                .collect(Collectors.toList());
    }

    public List<Movie> retrieveMovies_CF(List<Long> movieInfoIds) {
        var movieFutures = movieInfoIds
                .stream()
                .map(this::retrieveMovie_CF)
                .collect(Collectors.toList());
        return movieFutures
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<Movie> retrieveMovies_CF_single_stream(List<Long> movieInfoIds) {
        return movieInfoIds
                .stream()
                .map(this::retrieveMovie_CF)
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<Movie> retrieveMovies_CF_allOf(List<Long> movieInfoIds) {
        var movieFutures = movieInfoIds
                .stream()
                .map(this::retrieveMovie_CF)
                .collect(Collectors.toList());
        var cfAllOf =  CompletableFuture.allOf(
                movieFutures.toArray(new CompletableFuture[0]));
        return cfAllOf
                .thenApply(v -> movieFutures
                        .stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .join();
    }

    public CompletableFuture<Movie> retrieveMovie_CF(Long movieInfoId) {

        // movieInfo
        var movieInfo = CompletableFuture.supplyAsync( ()->invokeMovieInfoService(movieInfoId));

        // review
        var reviews = CompletableFuture.supplyAsync(()->invokeReviewsService(movieInfoId));

        return movieInfo
                .thenCombine(reviews, Movie::new);
    }

    private MovieInfo invokeMovieInfoService(Long movieInfoId) {

        // http://localhost:8080/movies
        //
        var moviesInfoUrlPath = "/v1/movie_infos/{movieInfoId}";

        return webClient
                .get()
                .uri(moviesInfoUrlPath, movieInfoId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .block();
    }

    private List<Review> invokeReviewsService(Long movieInfoId) {

        // http://localhost:8080/movies
        //
//        var reviewsUrlPath = "/v1/reviews?movieInfoId={movieInfoId}";

        var reviewUri = UriComponentsBuilder.fromUriString("/v1/reviews")
                .queryParam("movieInfoId", movieInfoId)
                .buildAndExpand()
                .toString();

        return webClient
                .get()
                .uri(reviewUri)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();
    }
}
