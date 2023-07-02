package kr.co.demojar.config.database.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = {"com.example"})
@PropertySources({
        /////
        @PropertySource(value = "classpath:/application.properties")
        /////
})
public class CommonDatabaseConfiguration {

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            ////
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory);
        log.info("init jpaTransactionManager.. {}", jpaTransactionManager.hashCode());
        return jpaTransactionManager;
    }

}
