package kr.co.demojar.config.database.prod;


import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DatabaseHealthCheckContext {

    public static final ConcurrentHashMap<String, HikariConfig> DB_CONFIGS = new ConcurrentHashMap<>();

    private static String MASTER_DB_KEY = null;

    public static void setMasterDbKey(String masterDbKey) {
        MASTER_DB_KEY = masterDbKey;
    }

    public static final Set<String> DEAD_DB_KEYS = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());


    public static String getDataSourceKey(boolean readOnly) {

        List<String> keySet = new ArrayList<>(DB_CONFIGS.keySet());

        Collections.shuffle(keySet);

        final String key = keySet.stream().filter(s -> {
            if (DEAD_DB_KEYS.contains(s)) {
                return false;
            }

            if (DB_CONFIGS.get(s) == null) {
                return false;
            }

            boolean thisDbReadOnly = DB_CONFIGS.get(s).isReadOnly();

            final boolean readOnlyMatch = (readOnly == thisDbReadOnly);

            log.debug("[{}] >>> readOnlyMatch => [{}]", s, readOnlyMatch);

            return readOnlyMatch;

        }).findAny().orElse(MASTER_DB_KEY);

        log.debug("key => [{}]", key);

        return key;
    }

}
