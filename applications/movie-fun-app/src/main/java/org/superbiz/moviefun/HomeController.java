package org.superbiz.moviefun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.superbiz.moviefun.albumsapi.AlbumInfo;
import org.superbiz.moviefun.albumsapi.AlbumsClient;
import org.superbiz.moviefun.moviesapi.MovieInfo;
import org.superbiz.moviefun.moviesapi.MoviesClient;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MoviesClient moviesClient;
    private final AlbumsClient albumsClient;

    public HomeController(MoviesClient moviesClient, AlbumsClient albumsClient) {
        this.moviesClient = moviesClient;
        this.albumsClient = albumsClient;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        moviesClient.seed();
        albumsClient.seed();
        model.put("movies", moviesClient.getMovies());
        model.put("albums", albumsClient.getAlbums());
        return "setup";
    }

    @GetMapping("/albums")
    public String showAlbums(Map<String, Object> model) {
        logger.debug("Getting all albums.");
        model.put("albums", albumsClient.getAlbums());
        return "albums";
    }

    @GetMapping("/albums/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        logger.debug("Getting album {}", albumId);
        model.put("album", albumsClient.findById(albumId));
        return "albumDetails";
    }

    @GetMapping("/movies")
    public @ResponseBody
    List<MovieInfo> listAllMovies() {
        logger.debug("Getting all movies.");
        return moviesClient.getMovies();
    }
}
