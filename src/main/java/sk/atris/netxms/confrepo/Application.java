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

    /**
     * .jar entry point
     *
     * @param args application startup arguments
     */
    public static void main(String[] args) {
        configureApplicationProfiles("jar");
        loadApplicationConfiguration();

        // run the application
        SpringApplication.run(Application.class, args);
    }

    /**
     * .war entry point
     *
     * @param application provided by Spring
     * @return application startup class
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        configureApplicationProfiles("war");
        loadApplicationConfiguration();

        return application.sources(Application.class);
    }

    /**
     * Configures active Spring profiles.
     * <p>
     * {@code jar} or {@code war} depending on running application type.<br>
     * {@code win} or {@code unix} depending on platform application is running on.
     *
     * @param runType application type
     */
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

    /**
     * Loads the application configuration.
     * <p>
     * If {@code application.properties} is present on the classpath, its loaded from there (in dev environment).<br>
     * {@code application.properties} file is not packaged in the final product.
     * <p>
     * In the final product, the config is loaded @{symmetry.conf}.<br>
     * The file location depends on the OS on which the application is deployed.
     */
    private static void loadApplicationConfiguration() {
        final String propertiesFileName = "netxms-config-repository.conf";

        // if we have application.properties on the classpath, Spring will automatically load those
        if (Application.class.getClassLoader().getResource("application.properties") != null) {
            System.out.println("Loading 'application.properties' from classpath");
            return;
        }

        // otherwise, we load external configuration file
        final String propertiesFilePath;

        if (isWindows()) {
            propertiesFilePath = "C:\\ProgramData\\" + propertiesFileName;
        } else {
            propertiesFilePath = "/etc/" + propertiesFileName;
        }

        System.out.println("Loading configuration file '" + propertiesFilePath + "'");

        // using try-with-resources, InputStream will be closed automatically after try block
        try (InputStream propertiesStream = new FileInputStream(propertiesFilePath)) {
            System.getProperties().load(propertiesStream);

        } catch (FileNotFoundException e) {
            System.out.println("Configuration file '" + propertiesFilePath + "' not found; application shutting down!");
            throw new IllegalStateException("Failed to read application configuration");

        } catch (IOException e) {
            System.out.println("Failed to read configuration file from '" + propertiesFilePath + "'; application shutting down!");
            throw new IllegalStateException("Failed to read application configuration");

        }

    }

    /**
     * Returns if current platform is Windows.
     *
     * @return {@code true} or {@code false} if running on Windows
     */
    private static boolean isWindows() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("windows")) {
            return true;
        } else {
            return false;
        }
    }

}
