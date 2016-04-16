package sk.atris.netxms.confrepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        final String springBanner = "off";
        final String databasePath = System.getProperty("database.file.path");
        final String databaseUser = System.getProperty("database.user");
        final String databasePass = System.getProperty("database.pass");
        final String databaseUrl = "jdbc:hsqldb:file:" + databasePath;

        System.out.println(databaseUrl);
        System.out.println(databaseUser);
        System.out.println(databasePass);

        // configure Spring properties
        System.setProperty("spring.main.banner-mode", springBanner);
        System.setProperty("spring.datasource.url", databaseUrl);
        System.setProperty("spring.datasource.username", databaseUser);
        System.setProperty("spring.datasource.password", databasePass);

        // run the application
        SpringApplication.run(Application.class, args);
    }
}
