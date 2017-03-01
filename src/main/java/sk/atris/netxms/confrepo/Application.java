package sk.atris.netxms.confrepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        configureApplicationProfiles("jar");
        loadApplicationConfiguration();

        // run the application
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        configureApplicationProfiles("war");
        loadApplicationConfiguration();

        return application.sources(Application.class);
    }

    private static void configureApplicationProfiles(String runType) {
        String profiles = runType;

        // set profile based on host OS
        if (isWindows()) {
            profiles += ",win";
        } else {
            profiles += ",unix";
        }

        // set the final profile as system profile
        System.setProperty("spring.profiles.active", profiles);
    }

    private static void loadApplicationConfiguration() {
        final String propertiesFileName = "netxms-config-repository.conf";

        if (Application.class.getClassLoader().getResource(propertiesFileName) != null) {
            // if we have application.properties on the classpath, load from there
            System.out.println("Loading '" + propertiesFileName + "' from classpath");

            InputStream propertiesStream = Application.class.getClassLoader().getResourceAsStream(propertiesFileName);
            if (propertiesStream == null) {
                System.out.println("Configuration file '" + propertiesFileName + "' not found on classpath; application shutting down!");
                throw new IllegalStateException("Failed to read application configuration");
            }

            loadProperties(propertiesStream, "classpath");

        } else {
            // otherwise, we load application.properties from external file
            final String propertiesFilePath;

            if (isWindows()) {
                propertiesFilePath = "C:\\ProgramData\\" + propertiesFileName;
            } else {
                propertiesFilePath = "/etc/" + propertiesFileName;
            }

            System.out.println("Loading properties file '" + propertiesFilePath + "'");

            InputStream propertiesStream = null;
            try {
                propertiesStream = new FileInputStream(propertiesFilePath);

            } catch (FileNotFoundException e) {
                System.out.println("Configuration file '" + propertiesFilePath + "' not found; application shutting down!");
                throw new IllegalStateException("Failed to read application configuration");

            }

            loadProperties(propertiesStream, "'" + propertiesFilePath + "'");
        }
    }

    private static boolean isWindows() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("windows")) {
            return true;
        } else {
            return false;
        }
    }

    private static void loadProperties(InputStream propertiesStream, String filePathDescription) {
        try {
            System.getProperties().load(propertiesStream);

        } catch (IOException e) {
            System.out.println("Failed to read configuration file from " + filePathDescription + "; application shutting down!");
            throw new IllegalStateException("Failed to read application configuration");

        } finally {
            if (propertiesStream != null) {
                try {
                    propertiesStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
