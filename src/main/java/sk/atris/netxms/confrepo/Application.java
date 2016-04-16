package sk.atris.netxms.confrepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
        System.setProperty("spring.main.banner-mode", "off");

		SpringApplication.run(Application.class, args);
	}
}
