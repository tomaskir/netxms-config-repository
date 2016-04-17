package sk.atris.netxms.confrepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static final String springBannerMode = "off";

    public static void main(String[] args) {
        // configure Spring properties
        System.setProperty("spring.main.banner-mode", springBannerMode);

        // run the application
        SpringApplication.run(Application.class, args);
    }
}
