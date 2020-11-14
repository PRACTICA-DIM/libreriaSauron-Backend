package es.lanyu.sauron.config;

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Esta clase de configuracion se utiliza para cargar un {@link KeycloakDeployment}
 * con los valores que figuran en el archivo de propiedades con el prefijo keycloak.
 * @author ACING DIM XLII
 * @version v1.0.0
 * @see org.keycloak.adapters.KeycloakDeployment
 * @see org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
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
