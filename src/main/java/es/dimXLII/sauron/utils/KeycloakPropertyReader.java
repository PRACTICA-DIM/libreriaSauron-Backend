package es.dimXLII.sauron.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author ACING DIM XLII
 *
 * <p>Esta clase de configuracion proporciona un bean inyectado del entorno de ejecución
 *  de la aplicación y proporciona la lectura de las propiedades indicadas en el fichero
 *   de propiedades del classpath.</p>
 */
@Configuration
@PropertySource("classpath:sauron.properties")
public class KeycloakPropertyReader {

    @Autowired
    private Environment env;

    public String getProperty(String key) {
        return env.getProperty(key);
    }

}