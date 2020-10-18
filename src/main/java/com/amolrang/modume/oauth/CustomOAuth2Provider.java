package com.amolrang.modume.oauth;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

public enum CustomOAuth2Provider {

    KAKAO {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationID) {
            return getBuilder("kakao", ClientAuthenticationMethod.POST)
                    .scope("profile","account_email") // 요청할 권한
                    .authorizationUri("https://kauth.kakao.com/oauth/authorize") // authorization code 얻는 API
                    .tokenUri("https://kauth.kakao.com/oauth/token") // access Token 얻는 API
                    .userInfoUri("https://kapi.kakao.com/v2/user/me") // 유저 정보 조회 API
                    .clientId("a81cf46edcab4d18e69068167b660345")
                    .clientSecret("H45ITgTIbIJSX6bEAJTCAgZ5nozeMLEU")
                    .userNameAttributeName("id") // userInfo API Response에서 얻어올 ID 프로퍼티
                    .clientName("kakao"); // spring 내에서 인식할 OAuth2 Provider Name
        }
    }, 
    NAVER{
	    @Override
	    public ClientRegistration.Builder getBuilder(String registrationID) {
	         ClientRegistration.Builder builder = getBuilder("naver", ClientAuthenticationMethod.BASIC)
	                //.scope("user:edit","user:read:email") // 요청할 권한
	                .authorizationUri("https://nid.naver.com/oauth2.0/authorize") // authorization code 얻는 API
	                .tokenUri("https://nid.naver.com/oauth2.0/token") // access Token 얻는 API
	                .userInfoUri("https://openapi.naver.com/v1/nid/me") // 유저 정보 조회 API
	                .clientId("xszt5SB2ywYHD8jD4Tlh")
	                .clientSecret("ScrP_LkFDZ")
	                //임시조치
	                .userNameAttributeName("response") // userInfo API Response에서 얻어올 ID 프로퍼티
	                .clientName("naver"); // spring 내에서 인식할 OAuth2 Provider Name
	         return builder;
	    }
	};
	
    private static final String DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}";
    
    protected final ClientRegistration.Builder getBuilder(String registrationId,
                                                          ClientAuthenticationMethod method) {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
        builder.clientAuthenticationMethod(method);
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.redirectUriTemplate(CustomOAuth2Provider.DEFAULT_LOGIN_REDIRECT_URL);
        return builder;
    }

    public abstract ClientRegistration.Builder getBuilder(String registrationID);
    
}