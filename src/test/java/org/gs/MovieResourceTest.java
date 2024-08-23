package org.gs;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@QuarkusTest
public class MovieResourceTest {

    @InjectMocks
    MovieRepository movieRepository;

    @Inject
    MovieResource movieResource;

    private Movie movie;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAll() {
        Movie movie = new Movie();
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.of(movie));

        given()
          .when().get("/movies/1")
          .then()
             .statusCode(200);

        verify(movieRepository).findByIdOptional(1L);
    }

    @Test
    public void testGetByIdKO() {
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        given()
          .when().get("/movies/1")
          .then()
             .statusCode(404);

        verify(movieRepository).findByIdOptional(1L);
    }

    @Test
    public void testGetByTitleOK() {
        Movie movie = new Movie();
        when(movieRepository.find("title", "Inception").singleResultOptional()).thenReturn(Optional.of(movie));

        given()
          .when().get("/movies/title/Inception")
          .then()
             .statusCode(200);

        verify(movieRepository).find("title", "Inception");
    }

    @Test
    public void testGetByTitleKO() {
        when(movieRepository.find("title", "Inception").singleResultOptional()).thenReturn(Optional.empty());

        given()
          .when().get("/movies/title/Inception")
          .then()
             .statusCode(404);

        verify(movieRepository).find("title", "Inception");
    }

    @Test
    public void getByCountry() {
        List<Movie> movies = Arrays.asList(new Movie(), new Movie());
        when(movieRepository.findByCountry("USA")).thenReturn(movies);

        given()
          .when().get("/movies/country/USA")
          .then()
             .statusCode(200)
             .body("size()", is(2));

        verify(movieRepository).findByCountry("USA");
    }

    @Test
    public void testCreateOK() {
        Movie movie = new Movie();
        movie.setId(1L);
        doNothing().when(movieRepository).persist(movie);
        when(movieRepository.isPersistent(movie)).thenReturn(true);

        given()
          .contentType(ContentType.JSON)
          .body(movie)
          .when().post("/movies")
          .then()
             .statusCode(201)
             .header("Location", is("/movies/1"));

        verify(movieRepository).persist(movie);
        verify(movieRepository).isPersistent(movie);
    }

    @Test
    public void testCreateKO() {
        Movie movie = new Movie();
        doNothing().when(movieRepository).persist(movie);
        when(movieRepository.isPersistent(movie)).thenReturn(false);

        given()
          .contentType(ContentType.JSON)
          .body(movie)
          .when().post("/movies")
          .then()
             .statusCode(400);

        verify(movieRepository).persist(movie);
        verify(movieRepository).isPersistent(movie);
    }

    @Test
    public void testUpdateByIdOK() {
        Movie movie = new Movie();
        movie.setTitle("Updated Title");
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.of(movie));

        given()
          .contentType(ContentType.JSON)
          .body(movie)
          .when().put("/movies/1")
          .then()
             .statusCode(200)
             .body("title", is("Updated Title"));

        verify(movieRepository).findByIdOptional(1L);
    }

    @Test
    public void testUpdateByIdKO() {
        Movie movie = new Movie();
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        given()
          .contentType(ContentType.JSON)
          .body(movie)
          .when().put("/movies/1")
          .then()
             .statusCode(404);

        verify(movieRepository).findByIdOptional(1L);
    }

    @Test
    public void testDeleteByIdOK() {
        when(movieRepository.deleteById(1L)).thenReturn(true);

        given()
          .when().delete("/movies/1")
          .then()
             .statusCode(204);

        verify(movieRepository).deleteById(1L);
    }

    @Test
    public void testDeleteByIdKO() {
        when(movieRepository.deleteById(1L)).thenReturn(false);

        given()
          .when().delete("/movies/1")
          .then()
             .statusCode(404);

        verify(movieRepository).deleteById(1L);
    }
}