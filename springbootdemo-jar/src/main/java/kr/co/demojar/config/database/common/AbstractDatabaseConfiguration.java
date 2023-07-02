package kr.co.demojar.config.database.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;

public class AbstractDatabaseConfiguration {

    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected final HikariConfig makeHikariConfig(final String driverClassName, final DatabaseSourceProperty.DatabaseInfo databaseInfo) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(driverClassName);

        hikariConfig.setJdbcUrl(databaseInfo.getUrl());
        hikariConfig.setUsername(databaseInfo.getUsername());
        hikariConfig.setPassword(databaseInfo.getPassword());
        hikariConfig.setMaximumPoolSize(databaseInfo.getMaximumPoolSize());
        hikariConfig.setReadOnly(databaseInfo.isReadonly());

        hikariConfig.setSchema(databaseInfo.getSchema());
        hikariConfig.setMaxLifetime(databaseInfo.getMaxLifetime());
//        hikariConfig.setKeepaliveTime(databaseInfo.getMaxLifetime());

        return hikariConfig;
    }

}
