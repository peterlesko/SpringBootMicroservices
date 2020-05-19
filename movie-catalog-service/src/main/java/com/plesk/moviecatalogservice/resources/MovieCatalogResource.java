package com.plesk.moviecatalogservice.resources;

import com.plesk.moviecatalogservice.models.CatalogueItem;
import com.plesk.moviecatalogservice.models.Movie;
import com.plesk.moviecatalogservice.models.Rating;
import com.plesk.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogueItem> getCatalog(@PathVariable ("userId") String userId) {

        UserRating ratings = restTemplate.getForObject("http://movie-data-service/ratingsdata/users/" + userId, UserRating.class);
        //UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
            // For each movie ID, call info service and get details
            Movie movie = restTemplate.getForObject("http://movie-info-service/localhost:8082/movies/" + rating.getMovieId(), Movie.class );
            // Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class );
            // Put them all together
            return new CatalogueItem(movie.getName(), "Desc", rating.getRating());
        })
                .collect(Collectors.toList());
    }
}

/*            Movie movie = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/movies/" + rating.getMovieId())
                .retrieve()
                .bodyToMono(Movie.class)
                .block();*/