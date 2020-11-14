package es.lanyu.sauron;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.lanyu.sauron.config.KeycloakAdminClientConfig;
import es.lanyu.sauron.utils.KeycloakAdminClientUtils;
import es.lanyu.sauron.utils.KeycloakPropertyReader;

/**
 * Esta clase es un servicio, especialización de @Component, que
 * proporciona un usuario {@link SauronUser} autenticado y sus roles, también proporciona un {@link SauronUserRepresentation} mediante
 * el método {@link getUserProfileOfLoggedUser()}, así como todos los roles del Realm donde se encuentra securizado el cliente mediante
 * el método {@link getRolesRealm()}.
 * @author ACING DIM XLII
 * @version v1.0.1
 * @see es.lanyu.sauron.SauronUser
 * @see es.lanyu.sauron.SauronUserRepresentation
 * 
 */
@Service
public class SauronServiceImpl implements SauronService {

	@Autowired
	private KeycloakPropertyReader keycloakPropertyReader;
	
	@Value("${sauron.keycloak-admin}")
	private String clientKeycloakAdmin;
	
	@Value("${sauron.client-secret}")
	private String clientSecret;
	
	@Value("${keycloak.auth-server-url}")//esta propiedad la extrae del properties del backend que securiza.
	private String urlServer;
	
	@Value("${keycloak.realm}")//esta propiedad la extrae del properties del backend que securiza.
	private String realm;
		
	public List<String> getCurrentUserRoles() {
		return getSauronUser().getRoles();
	}

	@Override
	public SauronUser getSauronUser() {
		return getCurrentUser();
	}

	@SuppressWarnings("unchecked")
	public SauronUser getCurrentUser() {

		SauronUserImpl result = new SauronUserImpl();

		KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal = (KeycloakPrincipal<RefreshableKeycloakSecurityContext>) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
				.getContext().getAuthentication().getAuthorities();

		result.setUserId(principal.getKeycloakSecurityContext().getToken().getSubject());
		result.setUsername(principal.getKeycloakSecurityContext().getToken().getPreferredUsername());
		result.setEmail(principal.getKeycloakSecurityContext().getToken().getEmail());
		result.setFirstname(principal.getKeycloakSecurityContext().getToken().getFamilyName());
		result.setLastname(principal.getKeycloakSecurityContext().getToken().getGivenName());
		result.setRoles(authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList()));

		return result;
	}

	@SuppressWarnings("unchecked")
	public SauronUserRepresentation getUserProfileOfLoggedUser() {

		KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal = (KeycloakPrincipal<RefreshableKeycloakSecurityContext>) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		KeycloakAdminClientConfig keycloakAdminClientConfig = KeycloakAdminClientUtils
				.loadConfig(keycloakPropertyReader);
		Keycloak keycloak = KeycloakAdminClientUtils.getKeycloakClient(principal.getKeycloakSecurityContext(),
				keycloakAdminClientConfig);

		// Get realm
		RealmResource realmResource = keycloak.realm(keycloakAdminClientConfig.getRealm());
		UsersResource usersResource = realmResource.users();
		UserResource userResource = usersResource.get(getCurrentUser().getUserId());
		
		UserRepresentation userRepresentation = userResource.toRepresentation();
		userRepresentation.setRealmRoles(getCurrentUser().getRoles());

		return new SauronUserRepresentation(userRepresentation);
	}
	
	@Override
	public List<String> getRolesRealm() {

		// Accedo al servidor construyendo una instancia al cliente keycloak-admin que es una cuenta
		// cuya única misión es proporcionar datos del servidor. Realiza la verificación del backend
		// mediante credentials secret, no es necesario ningun usuario autenticado.
		Keycloak keycloak = KeycloakBuilder.builder()
			    .serverUrl(urlServer)
			    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
			    .realm(realm)
			    .clientId(clientKeycloakAdmin)
			    .clientSecret(clientSecret)
			    .resteasyClient(
			        new ResteasyClientBuilder()
			            .connectionPoolSize(10).build()
			    ).build();

		keycloak.tokenManager().getAccessToken();
		
		// Get realm
		RealmResource realmResource = keycloak.realm(realm);

		RolesResource rolesResource = realmResource.roles();

		List<RoleRepresentation> roleRepresentationList = rolesResource.list();

		List<String> roles = roleRepresentationList.stream().map(r -> r.getName().toUpperCase()).collect(Collectors.toList());
		
		keycloak.close();
				
		return roles;
		
	}
	
}