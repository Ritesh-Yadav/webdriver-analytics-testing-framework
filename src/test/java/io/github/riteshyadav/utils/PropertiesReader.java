package io.github.riteshyadav.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    public Properties getProperties(String environment){

        Properties prop = new Properties();
        String propFileName = String.format("%s/config.properties", environment);

        try(final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)){
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException(String.format("Property file '%s' not found in the classpath", propFileName));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return prop;
    }
}
