package kr.co.demojar.config.database.prod;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kr.co.demojar.config.common.YamlLoadFactory;
import kr.co.demojar.config.database.common.AbstractDatabaseConfiguration;
import kr.co.demojar.config.database.common.DatabaseSourceProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("prod")
@PropertySources({
        /////
        @PropertySource(value = "classpath:/application.yml", factory = YamlLoadFactory.class)
        /////
})
public class ProdDatabaseConfiguration extends AbstractDatabaseConfiguration {

    @Autowired
    private DatabaseSourceProperty databaseSourceProperty;

    /**
     * <PRE>
     * 가상 datasource
     * 실제로 DB에 쿼리를 요청할때 어느 쪽으로 요청할지 결정된다.
     * </PRE>
     *
     * @return
     */
    @Bean
    public DataSource routingDataSource() {

        List<DatabaseSourceProperty.DatabaseInfo> clusters = new ArrayList<>(databaseSourceProperty.getClusters());

        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                String dataSourceType = RoutingDataSourceManager.getCurrentDataSourceName();

                if (TransactionSynchronizationManager
                        .isActualTransactionActive()) {
                    boolean readOnly = TransactionSynchronizationManager
                            .isCurrentTransactionReadOnly();
                    dataSourceType = DatabaseHealthCheckContext.getDataSourceKey(readOnly);
                }

                log.info("dataSourceKey => [{}]", dataSourceType);

                RoutingDataSourceManager.removeCurrentDataSourceName();
                return dataSourceType;
            }
        };

        Map<Object, Object> targetDataSources = new HashMap<>();

        final String driverClassName = databaseSourceProperty.getDriverClassName();

        System.out.println("gson.toJson(databaseSourceProperty) = " + gson.toJson(databaseSourceProperty));

        DataSource masterDataSource = null;

        for (int i = 0; i < clusters.size(); i++) {
            DatabaseSourceProperty.DatabaseInfo cluster = clusters.get(i);
            final String clusterName = cluster.getName();
            HikariConfig hikariConfig = makeHikariConfig(driverClassName, cluster);
            DataSource dataSource = new HikariDataSource(hikariConfig);

            if (masterDataSource == null && !cluster.isReadonly()) {
                masterDataSource = dataSource;
                DatabaseHealthCheckContext.setMasterDbKey(clusterName);
            }
            targetDataSources.put(clusterName, dataSource);
            DatabaseHealthCheckContext.DB_CONFIGS.put(clusterName, hikariConfig);
        }

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Bean
    public DataSource dataSource(
            ////
            @Qualifier(value = "routingDataSource") DataSource routingDataSource
            ////
    ) {
        log.info("datasource init");
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }


    /**
     * <PRE>
     * multiple datasource 지원하는 entityManagerFactory.
     * </PRE>
     *
     * @param customerJpaProperties
     * @param dataSource
     * @return
     */
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            ////
            final JpaProperties customerJpaProperties,
            ////
            @Qualifier("dataSource") DataSource dataSource
            ////
    ) {

        Map<String, String> properties = customerJpaProperties.getProperties();

        log.info("customerJpaProperties = " + customerJpaProperties);
        log.info("customerJpaProperties " + gson.toJson(properties));

        EntityManagerFactoryBuilder builder ////
                = new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(),  ////
                properties,  ////
                null ////
        );

        return builder ////
                .dataSource(dataSource) ////
                .packages("kr.co") ////
//                .persistenceUnit("userEntityManager") ////
                .build();
    }


}
