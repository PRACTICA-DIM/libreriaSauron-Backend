# **CLIENTE-ADAPTADOR SAURON PARA SECURIZAR CLIENTES BACKEND**

Contiene todo el código en lenguaje JAVA para securizar el backend API-REST de aplicaciones.

Este proyecto contiene un cliente-adaptador que utiliza el framework Springboot (Spring Security) e implementa la seguridad necesaria para poder utilizar el producto Keycloak mediante el [adaptador JAVA](https://wjw465150.gitbooks.io/keycloak-documentation/content/securing_apps/topics/oidc/java/java-adapters.html) para aplicaciones Springboot ( SpringSecurity ).

La configuración de seguridad para una API REST (backend) se consigue añadiendo en su archivo ``build.gradle`` la dependencia a esta librería que esta publicada en [![](https://jitpack.io/v/PRACTICA-DIM/libreriaSauron-Backend.svg)](https://jitpack.io/#PRACTICA-DIM/libreriaSauron-Backend).

Dicha librería *(libreriaSauron-Backend)* contiene el código necesario para implementar la autenticación y autorización de usuarios, así como las interfaces necesarias para obtener los datos de identidad y los roles del usuario autenticado en un Gestor de Identidad y Acceso (IAM) o Servidor **Sauron**.
>- Dentro de la librería nos encontramos los siguientes **componentes**:
>>- <u>**Configuración de Seguridad**</u>: paquete ``es.lanyu.sauron.config`` incluye las **clases de configuración** ``SauronSecurityConfig`` *(implementa la configuración global de seguridad para utilizar el servidor Sauron)*, ``SauronAdminClientConfig`` *(implementa la configuración del cliente o aplicación)*, ``CustomSauronSpringBootConfigResolver`` *(implementación para indicar a la configuración global de seguridad las propiedades que se tienen que utilizar)*,
>>- <u>**Utilidades**</u>: paquete ``es.lanyu.sauron.utils`` incluye utilidades como la clase ``SauronAdminClientUtils`` *(para poder cargar la configuración de seguridad utilizando un constructor de configuración de seguridad y obtener una instancia del cliente en el servidor Sauron)* y la clase ``SauronPropertyReader`` *(proporcionando un lector del archivo de propiedades para cargar los parámetros necesarios para la configuración de seguridad)*,
>>- <u>**Interfaces de Usuario**</u>: paquete ``es.lanyu.sauron`` incluye la interfaz ``SauronService`` y el servicio ``SauronServiceImpl`` *(proporciona el usuario autenticado mediante la inyección de SauronPropertyReader, sus roles y todos los roles de la aplicación)*, así como la interfaz ``SauronUser`` y su clases de implementación ``SauronUserImpl`` y ``SauronUserRepresentation`` *(proporcionan el objeto usuario de Sauron con todos los atributos que contienen la información del usuario autenticado)*.
>>- <u>**Fichero ``sauron.properties``**</u>: proporciona la **configuración de propiedades de keycloak** en el archivo [sauron.properties](./src/main/resources/sauron.properties),
>>>- <u>**Fichero ``build.gradle``**</u>: el cliente hace uso de las siguientes dependencias para poder proporcionar la seguridad dentro de SpringSecurity utilizando el producto Keycloak (en negrita). El resto de dependencias son las necesarias para implementación dentro del framework de Springboot:
>>>     - **compile group: 'org.keycloak', name: 'keycloak-spring-boot-starter', version: '11.0.2'**
>>>     - **compile group: 'org.keycloak', name: 'keycloak-admin-client', version: '11.0.2'**
>>>     - implementation 'org.springframework.boot:spring-boot-starter-security'
>>>     - developmentOnly 'org.springframework.boot:spring-boot-devtools'
>>>     - compile group: 'org.apache.commons', name: 'commons-lang3', version: '3.11'

---

> Aviso: Al ser un repositorio utilizado para poder implementar seguridad para cualquier backend el código generado se encuentra comentado con el objeto de orientar al desarrollador de la aplicación.

---

