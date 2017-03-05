package sk.atris.netxms.confrepo.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Value("${database.file.path}")
    private String dbFilePath;

    @Value("${database.user}")
    private String dbUser;

    @Value("${database.pass}")
    private String dbPass;

    @Bean
    public DataSource configureDatabase() {
        // TODO: fail if DB connection info not configured in application.properties

        final String databaseUrl = "jdbc:hsqldb:file:" + dbFilePath;

        return DataSourceBuilder
                .create()
                .url(databaseUrl)
                .username(dbUser)
                .password(dbPass)
                .build();
    }

}
