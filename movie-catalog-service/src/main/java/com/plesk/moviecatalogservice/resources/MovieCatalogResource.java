package com.plesk.moviecatalogservice.resources;

import com.plesk.moviecatalogservice.models.CatalogueItem;
import com.plesk.moviecatalogservice.models.Movie;
import com.plesk.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

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

        //get all rated movie IDs
        List<Rating> ratings = Arrays.asList(
            new Rating("1234", 4 ),
            new Rating("5678", 3)
        );

        return ratings.stream().map(rating -> {
           // Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class );
            Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();

            return new CatalogueItem(movie.getName(), "Des", rating.getRating());
        })
        .collect(Collectors.toList());

        // For each movie ID, call info service and get details
        // Put them all together
    }
}
