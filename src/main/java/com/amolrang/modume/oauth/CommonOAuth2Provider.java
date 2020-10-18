package com.amolrang.modume.oauth;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

public enum CommonOAuth2Provider {
	GOOGLE {
		// API KEY
		// AIzaSyAr2aW-trtWelUhL-bxfHgcztE1AAxFKiU
		@Override
		public Builder getBuilder(String registrationID) {
			ClientRegistration.Builder builder = getBuilder(registrationID, ClientAuthenticationMethod.BASIC);
			builder.scope("openid", "profile", "email");
			builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
			builder.tokenUri("https://www.googleapis.com/oauth2/v4/token");
			builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
			builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo");
			builder.userNameAttributeName(IdTokenClaimNames.SUB);
			builder.clientName("Google");
			builder.clientId("327498212271-k08jmuqgri0octbl5tt78h749pf5irg5.apps.googleusercontent.com");
			builder.clientSecret("sB_WQ5-BCPHXcvEcSjoeUyI8");
			return builder;
		}
	},
	TWITCH{
	    @Override
	    public ClientRegistration.Builder getBuilder(String registrationID) {
	    	return getBuilder("twitch", ClientAuthenticationMethod.POST)
			.scope("user:edit","user:read:email"/*,"openid"*/)
			.authorizationUri("https://id.twitch.tv/oauth2/authorize")
			.tokenUri("https://id.twitch.tv/oauth2/token")
			.userInfoUri("https://id.twitch.tv/oauth2/userinfo")
			.userNameAttributeName(IdTokenClaimNames.SUB)
			.clientName("twitch")
			.clientId("nb7cdnmp6y4wuqcnfh31c92oh0k8l7")
			.clientSecret("u3cpdd9wnlnrmkh1fh8ar4kr7iu12n");
	    }
	};
	
	

    private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}";


	protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method) {

		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
		builder.clientAuthenticationMethod(method);
		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
		builder.redirectUriTemplate(CommonOAuth2Provider.DEFAULT_REDIRECT_URL);
		return builder;
	}

	public abstract Builder getBuilder(String registrationID);
}
