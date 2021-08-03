//package com.dohung.orderfood.config;
//
//
//import com.dohung.orderfood.security.SecurityUtils;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Optional;
//
//@Component
//public class AuthenticateClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
//
//    @Override
//    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
//        String token = SecurityUtils.getCurrentUserJWT();
//        httpRequest.getHeaders().add("Authorization","Bearer "+token);
//        return clientHttpRequestExecution.execute( httpRequest, bytes );
//    }
//
//    public static Optional<String> getCurrentUserToken() {
////        SecurityContext securityContext = SecurityContextHolder.getContext();
////        return Optional.ofNullable(securityContext.getAuthentication())
////            .filter(authentication -> authentication.getDetails() instanceof OAuth2AuthenticationDetails)
////            .map(authentication -> ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
//            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
//            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId,
//                oauthToken.getName());
//            return client.getAccessToken().getTokenValue();
//        }
//        return null;
//    }
//}
