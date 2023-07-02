package kr.co.demojar.config.database.dev;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kr.co.demojar.config.common.YamlLoadFactory;
import kr.co.demojar.config.database.common.AbstractDatabaseConfiguration;
import kr.co.demojar.config.database.common.DatabaseSourceProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("!prod")
@PropertySources({
        /////
        @PropertySource(value = "classpath:/application.yml", factory = YamlLoadFactory.class)
        /////
})
public class DevDatabaseConfiguration extends AbstractDatabaseConfiguration {

    @Autowired
    private DatabaseSourceProperty databaseSourceProperty;

    @Bean
    public DataSource dataSource() {

        log.info("datasource init");

        List<DatabaseSourceProperty.DatabaseInfo> clusters = new ArrayList<>(databaseSourceProperty.getClusters());

        final String driverClassName = databaseSourceProperty.getDriverClassName();

        DatabaseSourceProperty.DatabaseInfo cluster = clusters.get(0);
        final String clusterName = cluster.getName();
        HikariConfig hikariConfig = makeHikariConfig(driverClassName, cluster);

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        log.info("               JdbcUrl :: {}", hikariDataSource.getJdbcUrl());
        log.info("       DriverClassName :: {}", hikariDataSource.getDriverClassName());
        log.info("  UserName && password :: {}", hikariDataSource.getUsername() + "  ..." + (hikariDataSource.getPassword() == null ? "null" : hikariDataSource.getPassword().length()));
        log.info(" init hikariDataSource :: {}", hikariDataSource.hashCode());

        return hikariDataSource;
    }

    /**
     * <PRE>
     * 아래 entityManagerFactory 는 multiple datasource 지원하지 않는다.
     * </PRE>
     *
     * @param builder
     * @param dataSource
     * @return
     */
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            ////
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource
    ) {

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = builder
                .dataSource(dataSource)   //
                .packages("kr.co").build();

        Map<String, Object> propMap = localContainerEntityManagerFactoryBean.getJpaPropertyMap();
        log.info("toJson(propMap) = " + new Gson().toJson(propMap));

        log.info("init LocalContainerEntityManagerFactoryBean.. {}", localContainerEntityManagerFactoryBean.hashCode());
        return localContainerEntityManagerFactoryBean;
    }

}
