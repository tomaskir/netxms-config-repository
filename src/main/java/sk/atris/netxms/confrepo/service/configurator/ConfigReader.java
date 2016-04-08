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
public final class ConfigReader {
    @Getter
    private final static ConfigReader instance = new ConfigReader();

    private Properties properties;

    public String getReadOnlyAccessToken() {
        String filename = ApplicationConfiguration.CONFIG_FILE_NAME.toString();

        log.debug("Getting ReadOnly access token from '{}' property file on the classpath.", filename);

        return getTokenFromClasspathFile(filename, ApplicationConfiguration.READONLY_ACCESS_TOKEN_PROPERTY.toString());
    }

    public String getReadOnlyAccessToken(String filepath) {
        log.debug("Getting ReadOnly access token from '{}' property file (explicit location).", filepath);

        return getTokenFromFile(filepath, ApplicationConfiguration.READONLY_ACCESS_TOKEN_PROPERTY.toString());
    }

    public String getReadWriteAccessToken() {
        String filename = ApplicationConfiguration.CONFIG_FILE_NAME.toString();

        log.debug("Getting ReadWrite access token from '{}' property file on the classpath.", filename);

        return getTokenFromClasspathFile(filename, ApplicationConfiguration.READWRITE_ACCESS_TOKEN_PROPERTY.toString());
    }

    public String getReadWriteAccessToken(String filepath) {
        log.debug("Getting ReadWrite access token from '{}' property file (explicit location).", filepath);

        return getTokenFromFile(filepath, ApplicationConfiguration.READWRITE_ACCESS_TOKEN_PROPERTY.toString());
    }

    private String getTokenFromClasspathFile(String filename, String tokenName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            log.error("Error opening property file '{}'!", filename);
            return null;
        }

        String token = getToken(is, tokenName);

        try {
            is.close();
        } catch (IOException ignored) {
        }

        return token;
    }

    private String getTokenFromFile(String filepath, String tokenName) {
        try (InputStream fis = new FileInputStream(filepath)) {
            return getToken(fis, tokenName);
        } catch (IOException e) {
            log.error("Error opening property file '{}'!", filepath);
            return null;
        }
    }

    private String getToken(InputStream is, String tokenName) {
        properties = new Properties();

        try {
            properties.load(is);
        } catch (IOException e) {
            log.error("Error '{}' loading the property file!", e.getMessage());
            return null;
        }

        String tokenValue = properties.getProperty(tokenName);

        if (tokenValue == null) {
            log.error("Property '{}' not found in the property file!", tokenName);
            return null;
        } else if (tokenValue.equals("")) {
            // we do not allow empty tokens, so if an empty token was read, treat it as if no token was present
            log.warn("Found empty property '{}' in the property file, ignoring it!", tokenName);
            return null;
        }

        return tokenValue;
    }
}
