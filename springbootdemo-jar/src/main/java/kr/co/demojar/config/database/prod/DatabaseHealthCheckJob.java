package kr.co.demojar.config.database.prod;


import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DatabaseHealthCheckJob {

    @Scheduled(cron = "0/10 * * * * ?")
    public void execute() {
        Map<String, HikariConfig> dbConfigs = new HashMap<>(DatabaseHealthCheckContext.DB_CONFIGS);

        dbConfigs.forEach((dbName, hikariConfig) -> {

            Socket socket = null;
            try {
                String jdbcUrl = hikariConfig.getJdbcUrl().replace("jdbc:", "");

                URI uri = new URI(jdbcUrl);

                final String host = uri.getHost();
                final int port = uri.getPort();

                log.debug(" db [{}] host [{}] port [{}] ", dbName, host, port);

                socket = new Socket(host, port);

                final boolean wasDead = DatabaseHealthCheckContext.DB_CONFIGS.containsKey(dbName);

                if (wasDead) {
                    log.info(" db [{}] is connected !!", dbName);
                } else {
                    log.debug(" db [{}] is alive !!", dbName);
                }

                DatabaseHealthCheckContext.DEAD_DB_KEYS.remove(dbName);

            } catch (Exception e) {

                final boolean wasDead = DatabaseHealthCheckContext.DB_CONFIGS.containsKey(dbName);

                if (wasDead) {
                } else {
                    log.error(" db [{}] Socket Exception !! :: [{}] [{}]", dbName, e.toString(), e.getMessage());
                }
                DatabaseHealthCheckContext.DEAD_DB_KEYS.add(dbName);
            } finally {
                IOUtils.closeQuietly(socket);
            }

        });
    }
}
