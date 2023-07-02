package kr.co.demojar.config.database.prod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoutingDataSourceManager {

    private static final ThreadLocal<String> currentDataSourceName = new ThreadLocal<>();

    public static void setCurrentDataSourceName(String dataSourceType) {
        log.error("setCurrentDataSourceName [{}]", dataSourceType);
        currentDataSourceName.set(dataSourceType);
    }

    public static String getCurrentDataSourceName() {

//        final String dataSourceKey = DatabaseHealthCheckContext.getDataSourceKey(false);
//
//        currentDataSourceName.set(dataSourceKey);

        final String autoGet = currentDataSourceName.get();

        return autoGet;
    }

    public static void removeCurrentDataSourceName() {
        currentDataSourceName.remove();
    }
}