package es.dimXLII.sauron;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.dimXLII.sauron.config.KeycloakAdminClientConfig;
import es.dimXLII.sauron.utils.KeycloakAdminClientUtils;
import es.dimXLII.sauron.utils.KeycloakPropertyReader;

/**
 * @author ACING DIM XLII
 * @nota Esta clase es un servicio, especialización de @Component, que
 *       proporciona un usuario {@link SauronUser} y sus roles, a través del
 *       {@link CurrentUserProvider}.
 *
 */
@Service
public class SauronServiceImpl implements SauronService {

	@Autowired
	private KeycloakPropertyReader keycloakPropertyReader;

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
}