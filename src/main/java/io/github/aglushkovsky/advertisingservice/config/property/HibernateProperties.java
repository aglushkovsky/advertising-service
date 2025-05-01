package io.github.aglushkovsky.advertisingservice.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hibernate")
public class HibernateProperties {

    public static final String PHYSICAL_NAMING_STRATEGY_KEY = "hibernate.physical_naming_strategy";
    public static final String DIALECT_KEY = "hibernate.dialect";
    public static final String SHOW_SQL_KEY = "show_sql";
    public static final String FORMAT_SQL_KEY = "format_sql";

    private String username;
    private String password;
    private String url;
    private String driver;
    private String entityPackage;
    private String namingPhysicalStrategy;
    private String dialect;
    private Sql sql;

    @Data
    public static class Sql {
        private Boolean show;
        private Boolean format;
    }
}
