package es.lanyu.sauron.service;

import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

import es.lanyu.sauron.user.SauronUser;
import es.lanyu.sauron.user.SauronUserRepresentation;

/**
 * Esta interfaz proporciona un usuario autenticado en el servidor Sauron con su perfil
 * y sus roles, asi como la lista de todos los roles de la aplicación donde se encuentra el cliente securizado. 
 * @author ACING DIM XLII
 * @version v2.0.0 
 * @see SauronUser
 */
public interface SauronService {

	Keycloak getKeycloak();
	
	SauronUser getSauronUser();
	
	SauronUserRepresentation getUserProfile();
	
	RealmResource getRealmResource();
	
	UsersResource getUsersResource();
	
	List<String> getRolesRealm();

}