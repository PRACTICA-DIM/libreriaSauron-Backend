# **CLIENTE-ADAPTADOR SAURON PARA SECURIZAR CLIENTES BACKEND**

Este repositorio contiene todo el código en lenguaje JAVA para securizar el backend API-REST de aplicaciones que implementan el framework SpringBoot y utilizan el Gestor de Identidad y acceso *Sauron*.

El proyecto contiene un cliente-adaptador que utiliza el framework SpringBoot (Spring-Security) e implementa la seguridad necesaria para poder utilizar el producto Sauron mediante un [adaptador JAVA del producto Keycloak](https://wjw465150.gitbooks.io/keycloak-documentation/content/securing_apps/topics/oidc/java/java-adapters.html) para aplicaciones Springboot ( SpringSecurity ).

---

## PROCEDIMIENTO PARA SECURIZAR

El **procedimiento para securizar** cualquier Backend API-REST que actúa como servidor de recursos es muy simple, basta con realizar estas tareas:

1. Añadir en el archivo `build.gradle` de la API-REST la dependencia a la última versión de esta librería que esta publicada en [![](https://jitpack.io/v/PRACTICA-DIM/libreriaSauron-Backend.svg)](https://jitpack.io/#PRACTICA-DIM/libreriaSauron-Backend).

2. Incluir en el archivo `application.properties` las siguientes propiedades para permitir la comunicación con el servidor Sauron (también es posible generar un archivo de propiedades exclusivo, `sauron.properties`, separando las propiedades de `application.properties`, en este caso habrá que indicarlo según punto 3.):
    
```
# CONFIGURACION SECURIZACION CON SERVIDOR SAURON (variará en cada backend que se securiza, esta información será solicitada al administrador del servidor, el resto de propiedades que se necesitan están incluidas en la libreríaSauron-Backend).

# Nombre del Realm.

keycloak.realm = <nombre del Realm en servidor Sauron>

# Dirección del servidor de autorización Sauron.

keycloak.auth-server-url = <url-Servidor>/auth

# Client ID o nombre del backend en el servidor Sauron.

keycloak.resource = <nombre del cliente backend en servidor Sauron>

#Por motivos de seguridad, incluir esta propiedad como variable de entorno en la configuracion Run/Debug As para no exponerlo públicamente.
	# Credenciales para hacer uso del cliente admin-cli
		#Username del admin de Sauron
sauron.username-admin = <username>
		#Password del administrador del servidor Sauron
sauron.admin-pass = <password>


```
Ejemplo `application.properties`:

```
# CONFIGURACION SECURIZACION CON SERVIDOR SAURON (variará en cada backend que se securiza, esta información será solicitada al administrador del servidor).

keycloak.realm = adaptadorSauron
keycloak.auth-server-url = https://herokusauron.herokuapp.com/auth
keycloak.resource = adaptador-backend
sauron.username-admin = admin
sauron.admin-pass = Minisdef01

```

3. Por último, una vez configurado el archivo .properties, y **sólo en el caso de que se hayan configurado las propiedades en** `sauron.propeties` (archivo independiente para las propiedades de Sauron), indicaremos a *SpringBoot*, dentro de la clase que contiene el método `main` o una clase de configuración propia @Configuration, el classpath para que encuentre el archivo de propiedades a cargar. También, indicaremos la clase `SauronSecurityConfig.class` mediante un @Import para que cargue la configuración de seguridad y las propiedades internas de la libreríaSauron-Backend:

Ejemplo en archivo **Application.java**

```
@SpringBootApplication
@PropertySource("classpath:sauron.properties" )		//no necesario si se incluyen las propiedades en application.properties
@Import(SauronSecurityConfig.class)
public class Application {

	public static void main(String[] args) throws ParseException, IOException, URISyntaxException {

			SpringApplication.run(Application.class, args);

	}
}

```
---

## ESTRUCTURA DE LA LIBRERIA

La librería *(libreriaSauron-Backend)* proporciona el código necesario para implementar la autenticación y autorización de usuarios, así como las interfaces para obtener los datos de identidad, los roles del usuario autenticado y todos los roles de la aplicación que son gestionados mediante un servidor IAM  **Sauron**.
- Dentro de la librería nos encontramos los siguientes **componentes**:
>- <u>**Configuración de Seguridad**</u>: paquete ``es.lanyu.sauron.config`` incluye la **clase de configuración** ``SauronSecurityConfig`` *(implementa la configuración global de seguridad para utilizar el servidor Sauron)*,
>- <u>**Servicio Sauron**</u>: paquete ``es.lanyu.sauron.service`` incluye la interfaz ``SauronService`` y el servicio ``SauronServiceImpl`` *(proporcionando el usuario autenticado, sus roles, todos los roles de la aplicación, todos los usuarios de la aplicación y todos los usuarios con un rol específico)*,
>- <u>**Interfaces de Usuario**</u>: paquete ``es.lanyu.sauron.user`` incluye la interfaz ``SauronUser`` y su clases de implementación ``SauronUserImpl`` y ``SauronUserRepresentation`` *(proporcionan el objeto usuario de Sauron con todos los atributos que contienen la información del usuario autenticado)*.
>- <u>**Fichero ``sauronBasic.properties``**</u>: proporciona la **configuración de propiedades para Sauron** en el archivo [sauronBasic.properties](./src/main/resources/sauronBasic.properties),
>- <u>**Fichero ``build.gradle``**</u>: la librería utiliza las siguientes dependencias para poder proporcionar la seguridad dentro de SpringSecurity utilizando el adaptador Java que proporciona el producto Keycloak (en negrita), *la versión de estas dependencias deberá ser la misma que la versión utilizada por el servidor Sauron (evita problemas de incompatibilidad)*. El resto de dependencias son las necesarias para implementación dentro del framework de SpringBoot:
>     - **compile group: 'org.keycloak', name: 'keycloak-spring-boot-starter', version: '11.0.2'**
>     - **compile group: 'org.keycloak', name: 'keycloak-admin-client', version: '11.0.2'**
>     - implementation 'org.springframework.boot:spring-boot-starter-security'
>     - developmentOnly 'org.springframework.boot:spring-boot-devtools'
>     - compile group: 'org.apache.commons', name: 'commons-lang3', version: '3.11'



> *Aviso informativo*: Al ser un repositorio utilizado para poder implementar seguridad para cualquier backend el código generado se encuentra comentado con el objeto de orientar al desarrollador de la aplicación.

---

