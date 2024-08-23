package org.gs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private MovieRepository movieRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByCountry() {
        String country = "USA";
        Movie movie1 = new Movie();
        movie1.setCountry(country);
        Movie movie2 = new Movie();
        movie2.setCountry(country);
        List<Movie> expectedMovies = Arrays.asList(movie1, movie2);

        Query query = mock(Query.class);
        when(entityManager.createQuery("SELECT m FROM Movie m WHERE m.country = ?1 ORDER BY id DESC"))
            .thenReturn(query);
        when(query.setParameter(1, country)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedMovies);

        List<Movie> actualMovies = movieRepository.findByCountry(country);

        assertEquals(expectedMovies, actualMovies);
    }
}