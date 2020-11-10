package es.lanyu.sauron;

/**
 * Esta interfaz se tiene que utilizar para conseguir un usuario autenticado en el servidor Sauron. 
 * @author ACING DIM XLII
 * @version v1.0.0 
 * @see SauronUser
 */
public interface SauronService {

	SauronUser getSauronUser();

}