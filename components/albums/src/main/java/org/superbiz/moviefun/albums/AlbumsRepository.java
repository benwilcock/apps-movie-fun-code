package org.superbiz.moviefun.albums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class AlbumsRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addAlbum(Album album) {
        logger.debug("Creating album with title {}, and year {}", album.getTitle(), album.getYear());
        entityManager.persist(album);
    }

    public Album find(long id) {
        logger.debug("Finding album with id {}", id);
        return entityManager.find(Album.class, id);
    }

    public List<Album> getAlbums() {
        logger.debug("Getting all albums");
        CriteriaQuery<Album> cq = entityManager.getCriteriaBuilder().createQuery(Album.class);
        cq.select(cq.from(Album.class));
        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public void deleteAlbum(Album album) {
        entityManager.remove(album);
    }

    @Transactional
    public void updateAlbum(Album album) {
        entityManager.merge(album);
    }
}
