package org.superbiz.moviefun.movies;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.fasterxml.jackson.dataformat.csv.CsvSchema.ColumnType.NUMBER;
import static org.superbiz.moviefun.CsvUtils.readFromCsv;

@Component
public class MovieFixtures {

    private final ObjectReader objectReader;
    private ResourceLoader resourceLoader;

    @Autowired
    public MovieFixtures(ResourceLoader resourceLoader) {
        CsvSchema schema = CsvSchema.builder()
            .addColumn("title")
            .addColumn("director")
            .addColumn("genre")
            .addColumn("rating", NUMBER)
            .addColumn("year", NUMBER)
            .build();

        objectReader = new CsvMapper().readerFor(Movie.class).with(schema);
        this.resourceLoader = resourceLoader;
    }

    public List<Movie> load() {
        return readFromCsv(objectReader, "movie-fixtures.csv", resourceLoader);
    }
}
