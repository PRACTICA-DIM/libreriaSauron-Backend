package es.lanyu.sauron.config;

/**
 * Esta clase implementa la configuración de Sauron usando el patrón Constructor.
 * @author ACING DIM XLII
 * @version v1.0.2
 */
public class SauronAdminClientConfig {

	private String serverUrl;
	private String realm;
	private String clientId;
	private String clientSecret;
	
	private SauronAdminClientConfig(SauronAdminClientConfigBuilder builder) {
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

	public static class SauronAdminClientConfigBuilder {

		private String serverUrl;
		private String realm;
		private String clientId;
		private String clientSecret;
		
		public SauronAdminClientConfigBuilder () {}
		
		public SauronAdminClientConfigBuilder serverUrl(String serverUrl) {
			this.serverUrl = serverUrl;
			return this;
		}

		public SauronAdminClientConfigBuilder realm(String realm) {
			this.realm = realm;
			return this;
		}

		public SauronAdminClientConfigBuilder clientId(String clientId) {
			this.clientId = clientId;
			return this;
		}

		
		public SauronAdminClientConfigBuilder clientSecret(String clientSecret) {
			this.clientSecret = clientSecret;
			return this;
		}

		
		public SauronAdminClientConfig build() {
			return new SauronAdminClientConfig(this);
		}
		
	}

}