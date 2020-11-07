package es.dimXLII.sauron;

import java.util.List;
import org.keycloak.representations.idm.UserRepresentation;

public class SauronUserRepresentation implements SauronUser {

	UserRepresentation userRepresentation;
		
	public SauronUserRepresentation (UserRepresentation userRepresentation) {
		this.userRepresentation = userRepresentation;
	}
	
	private UserRepresentation getUserRepresentation() {
		return userRepresentation;
	}
	
	@Override
	public String getUserId() {

		return getUserRepresentation().getId();
	}

	@Override
	public String getFirstname() {

		return getUserRepresentation().getFirstName();
	}

	@Override
	public String getLastname() {

		return getUserRepresentation().getLastName();
	}

	@Override
	public List<String> getRoles() {

		return getUserRepresentation().getRealmRoles();
	}

	@Override
	public String getUsername() {
		return getUserRepresentation().getUsername();
	}

	@Override
	public String getEmail() {
		return getUserRepresentation().getEmail();
	}

}
