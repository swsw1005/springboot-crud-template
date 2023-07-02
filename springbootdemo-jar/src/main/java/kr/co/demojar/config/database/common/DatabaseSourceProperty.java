package kr.co.demojar.config.database.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "database")
public class DatabaseSourceProperty {

    private String driverClassName;

    private List<DatabaseInfo> clusters;

    @Getter
    @Setter
    public static class DatabaseInfo {
        private String name;
        private String url;
        private String username;
        private String password;
        private int maximumPoolSize = 3;
        private boolean readonly = false;
        private String schema = "";
        private int maxLifetime = 300000;
    }

}

