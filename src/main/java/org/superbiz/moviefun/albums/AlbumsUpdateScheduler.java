package org.superbiz.moviefun.albums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Clock;
import java.util.Optional;

@Configuration
@EnableAsync
@EnableScheduling
public class AlbumsUpdateScheduler {

    private static DataSource dataSource;
    private static final long SECONDS = 1000;
    private static final long MINUTES = 60 * SECONDS;
    private static JdbcTemplate template;

    private final AlbumsUpdater albumsUpdater;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public AlbumsUpdateScheduler(AlbumsUpdater albumsUpdater, @Qualifier("dataSource") DataSource dataSource) {
        this.albumsUpdater = albumsUpdater;
        this.dataSource = dataSource;
        this.template = new JdbcTemplate(dataSource);
    }


    @Scheduled(initialDelay = 15 * SECONDS, fixedRate = 2 * MINUTES)
    public void run() {
        try {
            logger.debug("Starting albums update");

            if(startAlbumSchedulerTask()) {
                albumsUpdater.update();
            }

            logger.debug("Finished albums update");

        } catch (Throwable e) {
            logger.error("Error while updating albums", e);
        }
    }

    private boolean startAlbumSchedulerTask() {
        int updatedRows = template.update(
                "UPDATE album_scheduler_task" +
                        " SET started_at = now()" +
                        " WHERE started_at IS NULL" +
                        " OR started_at < date_sub(now(), INTERVAL 2 MINUTE)"
        );

        return updatedRows > 0;
    }
}
