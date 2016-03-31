package sk.atris.netxms.confrepo.service.configurator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.enums.ApplicationConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigReader {
    @Getter
    private final static ConfigReader instance = new ConfigReader();

    private Properties properties;

    public String getReadOnlyAccessToken(String filepath) {
        log.debug("Getting ReadOnly access token from '{}' property file.", filepath);

        return getToken(filepath, ApplicationConfiguration.READONLY_ACCESS_TOKEN_PROPERTY.toString());
    }

    public String getReadWriteAccessToken(String filepath) {
        log.debug("Getting ReadWrite access token from '{}' property file.", filepath);

        return getToken(filepath, ApplicationConfiguration.READWRITE_ACCESS_TOKEN_PROPERTY.toString());
    }

    private String getToken(String filepath, String tokenName) {
        try {
            loadProperties(filepath);
        } catch (IOException e) {
            log.error("Error '{}' loading '{}' file!", e.getMessage(), filepath);
            return null;
        }

        String tokenValue = properties.getProperty(tokenName);

        if (tokenValue == null)
            log.error("Property '{}' not found in '{}' file!", tokenName, filepath);


        return tokenValue;
    }

    private void loadProperties(String filepath) throws IOException {
        InputStream fis = new FileInputStream(filepath);

        properties = new Properties();
        properties.load(fis);
    }
}
