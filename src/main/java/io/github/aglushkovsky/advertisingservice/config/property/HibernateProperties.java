package io.github.aglushkovsky.advertisingservice.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hibernate")
public class HibernateProperties {
    private String username;
    private String password;
    private String url;
    private String driver;
    private String entityPackage;
    private String namingPhysicalStrategy;
    private Sql sql;

    @Data
    public static class Sql {
        private Boolean show;
        private Boolean format;
    }
}
