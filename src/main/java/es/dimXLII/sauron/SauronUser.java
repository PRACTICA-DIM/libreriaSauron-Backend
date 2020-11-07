package es.dimXLII.sauron;

import java.util.List;

public interface SauronUser {

	String getUserId();

	String getUsername();
	
	String getFirstname();
	
	String getLastname();

	String getEmail();

	List<String> getRoles();

}