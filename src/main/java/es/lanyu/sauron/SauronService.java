package es.lanyu.sauron;

import java.util.List;

/**
 * Esta interfaz se tiene que utilizar para obtener un usuario autenticado en el servidor Sauron y 
 * la lista de todos los roles del Realm donde se encuentra el cliente securizado. 
 * @author ACING DIM XLII
 * @version v1.0.1 
 * @see SauronUser
 */
public interface SauronService {

	SauronUser getSauronUser();
	
	List<String> getRolesRealm();

}