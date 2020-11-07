package es.dimXLII.sauron.config;

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ACING DIM XLII
 * 
 * <p>Esta clase de configuracion se utiliza para cargar un despliegue del servidor Keycloak
 * con los valores que figuran en el archivo aplication.properties con el prefijo keycloak.</p>
 *
 */
@Configuration
public class CustomKeycloakSpringBootConfigResolver extends KeycloakSpringBootConfigResolver {
	
	private final KeycloakDeployment keycloakDeployment; 
	
	public CustomKeycloakSpringBootConfigResolver (KeycloakSpringBootProperties properties) {
		keycloakDeployment = KeycloakDeploymentBuilder.build(properties);
		
	}
	
	public KeycloakDeployment resolveDeployment (HttpFacade.Request facade) {
		return keycloakDeployment;
	}

}
