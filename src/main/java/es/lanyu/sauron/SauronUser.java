package es.lanyu.sauron;

import java.util.List;

/**
 * Esta interfaz proporciona los datos básicos y roles del usuario autenticado en el servidor Sauron. 
 * @author ACING DIM XLII
 * @version v1.0.0 
 */
public interface SauronUser {

	String getUserId();

	String getUsername();
	
	String getFirstname();
	
	String getLastname();

	String getEmail();

	List<String> getRoles();

}