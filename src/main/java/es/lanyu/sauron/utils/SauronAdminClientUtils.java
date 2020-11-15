package es.lanyu.sauron.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.lanyu.sauron.config.SauronAdminClientConfig;
import es.lanyu.sauron.config.SauronAdminClientConfig.SauronAdminClientConfigBuilder;

/**
 * Esta clase de utilidad permite cargar la configuración de Sauron utilizando
 * un lector de propiedades, así como el cliente de Sauron que se comunicará con
 * una instancia de Sauron mediante el contexto de seguridad y la clase de configuración de Sauron.
 * <br>También proporciona comportamiento para añadir y borrar un rol a un rol compuesto, y borrar un rol de una lista de roles
 * @author ACING DIM XLII
 * @version v1.0.2
 */
public class SauronAdminClientUtils {

    private static Logger log = LoggerFactory.getLogger(SauronAdminClientUtils.class);

    /**
     * Loads the sauron configuration from system property.
     * 
     * @return sauron configuration
     * @see SauronAdminClientConfig
     */
    public static SauronAdminClientConfig loadConfig(SauronPropertyReader sauronPropertyReader) {
    	    	
        	
		SauronAdminClientConfig.SauronAdminClientConfigBuilder builder = new SauronAdminClientConfigBuilder();

        try {
        	String keycloakServer = System.getProperty("keycloak.url");
            if (!StringUtils.isBlank(keycloakServer)) {
                builder = (SauronAdminClientConfigBuilder) builder.serverUrl(keycloakServer);

            } else {
                builder = (SauronAdminClientConfigBuilder) builder.serverUrl(sauronPropertyReader.getProperty("keycloak.auth-server-url"));
            }

            String realm = System.getProperty("keycloak.realm");
            if (!StringUtils.isBlank(realm)) {
                builder = (SauronAdminClientConfigBuilder) builder.realm(realm);

            } else {
                builder = (SauronAdminClientConfigBuilder) builder.realm(sauronPropertyReader.getProperty("keycloak.realm"));
            }

            String clientId = System.getProperty("keycloak.clientId");
            if (!StringUtils.isBlank(clientId)) {
                builder = (SauronAdminClientConfigBuilder) builder.clientId(clientId);

            } else {
                builder = (SauronAdminClientConfigBuilder) builder.clientId(sauronPropertyReader.getProperty("keycloak.resource"));
            }

            String clientSecret = System.getProperty("keycloak.secret");
            if (!StringUtils.isBlank(clientSecret)) {
                builder = (SauronAdminClientConfigBuilder) builder.clientSecret(clientSecret);

            } else {
                builder = (SauronAdminClientConfigBuilder) builder.clientSecret(sauronPropertyReader.getProperty("keycloak.credentials.secret"));
            }

        } catch (Exception e) {
            log.error("Error: Loading keycloak admin configuration => {}", e.getMessage());
        }

        SauronAdminClientConfig config = builder.build();
        log.debug("Found Sauron Configuration: {}", config);

        return config;
    }

    /**
     * It builds a {@link Keycloak} client from a given configuration. This client
     * is used to communicate with the Sauron instance via REST API.
     * 
     * @param session the security context
     * @param config  Sauron configuration
     * @return Sauron instance
     * @see Keycloak
     * @see SauronAdminClientConfig
     */
    public static Keycloak getKeycloakClient(KeycloakSecurityContext session, SauronAdminClientConfig config) {

        return KeycloakBuilder.builder() //
                .serverUrl(config.getServerUrl()) //
                .realm(config.getRealm()) //
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS) //
                .clientId(config.getClientId()) //
                .clientSecret(config.getClientSecret()) //
                .authorization(session.getTokenString()) //
                .build();
    }

    /**
     * Adds a role to a composite role. A composite role is just a role that
     * contains sub roles.
     * 
     * @param sauron                  	sauron instance
     * @param sauronAdminClientConfig 	sauron configuration
     * @param client                    client id
     * @param role                      role to be added
     * @param compositeRole             where the role will be added
     */
    public static void addRoleToListOf(Keycloak sauron, SauronAdminClientConfig sauronAdminClientConfig, String client, String role, String compositeRole) {

        final String clientUuid = sauron.realm(sauronAdminClientConfig.getRealm()).clients().findByClientId(client).get(0).getId();

        RolesResource rolesResource = sauron.realm(sauronAdminClientConfig.getRealm()).clients().get(clientUuid).roles();

        final List<RoleRepresentation> existingRoles = rolesResource.list();

        final boolean roleExists = existingRoles.stream().anyMatch(r -> r.getName().equals(role));

        if (!roleExists) {
            RoleRepresentation roleRepresentation = new RoleRepresentation();
            roleRepresentation.setName(role);
            roleRepresentation.setClientRole(true);
            roleRepresentation.setComposite(false);

            rolesResource.create(roleRepresentation);
        }

        final boolean compositeExists = existingRoles.stream().anyMatch(r -> r.getName().equals(compositeRole));

        if (!compositeExists) {
            RoleRepresentation compositeRoleRepresentation = new RoleRepresentation();
            compositeRoleRepresentation.setName(compositeRole);
            compositeRoleRepresentation.setClientRole(true);
            compositeRoleRepresentation.setComposite(true);

            rolesResource.create(compositeRoleRepresentation);
        }

        final RoleResource compositeRoleResource = rolesResource.get(compositeRole);

        final boolean alreadyAdded = compositeRoleResource.getRoleComposites().stream().anyMatch(r -> r.getName().equals(role));

        if (!alreadyAdded) {
            final RoleRepresentation roleToAdd = rolesResource.get(role).toRepresentation();
            compositeRoleResource.addComposites(Collections.singletonList(roleToAdd));
        }
    }

    /**
     * Removes a given role from a composite role.
     * 
     * @param sauron                 	sauron instance
     * @param sauronAdminClientConfig	sauron configuration
     * @param role                      role to be deleted
     * @param compositeRole             where the role should be deleted
     */
    public static void removeRoleInListOf(Keycloak sauron, SauronAdminClientConfig sauronAdminClientConfig, String role, String compositeRole) {

        final String clientUuid = sauron.realm(sauronAdminClientConfig.getRealm()).clients().findByClientId(sauronAdminClientConfig.getClientId()).get(0).getId();

        final RolesResource rolesResource = sauron.realm(sauronAdminClientConfig.getRealm()).clients().get(clientUuid).roles();

        final RoleResource compositeRoleResource = rolesResource.get(compositeRole);

        try {
            final RoleRepresentation roleToDelete = rolesResource.get(role).toRepresentation();
            compositeRoleResource.getRoleComposites().remove(roleToDelete);

        } catch (NotFoundException e) {
            log.warn("¡Rol {} no existe!", role);
        }
    }

    /**
     * Removes a role from a given list of roles.
     * 
     * @param listOfRoleRepresentation list of roles
     * @param roleToBeRemove           role to be remove from the list
     * @return List of RoleRepresentation
     */
    public static List<RoleRepresentation> removeRoleInList(List<RoleRepresentation> listOfRoleRepresentation, RoleRepresentation roleToBeRemove) {

        listOfRoleRepresentation.remove(roleToBeRemove);

        List<RoleRepresentation> updatedListRoleRepresentation = new ArrayList<>();
        for (RoleRepresentation roleRepresentationItem : listOfRoleRepresentation) {
            if (!roleToBeRemove.getName().equalsIgnoreCase(roleRepresentationItem.getName())) {
                updatedListRoleRepresentation.add(roleRepresentationItem);
            }
        }

        return updatedListRoleRepresentation;
    }
}
