package es.lanyu.sauron.config;

/**
 * Esta clase implementa la configuración de Keycloak usando el patrón Constructor.
 * @author ACING DIM XLII
 * @version v1.0.0
 */
public class KeycloakAdminClientConfig {

	private String serverUrl;
	private String realm;
	private String clientId;
	private String clientSecret;
	
	private KeycloakAdminClientConfig(KeycloakAdminClientConfigBuilder builder) {
		this.serverUrl = builder.serverUrl;
		this.realm = builder.realm;
		this.clientId = builder.clientId;
		this.clientSecret = builder.clientSecret;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getRealm() {
		return realm;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public static class KeycloakAdminClientConfigBuilder {

		private String serverUrl;
		private String realm;
		private String clientId;
		private String clientSecret;
		
		public KeycloakAdminClientConfigBuilder () {}
		
		public KeycloakAdminClientConfigBuilder serverUrl(String serverUrl) {
			this.serverUrl = serverUrl;
			return this;
		}

		public KeycloakAdminClientConfigBuilder realm(String realm) {
			this.realm = realm;
			return this;
		}

		public KeycloakAdminClientConfigBuilder clientId(String clientId) {
			this.clientId = clientId;
			return this;
		}

		
		public KeycloakAdminClientConfigBuilder clientSecret(String clientSecret) {
			this.clientSecret = clientSecret;
			return this;
		}

		
		public KeycloakAdminClientConfig build() {
			return new KeycloakAdminClientConfig(this);
		}
		
	}

}