package com.dohung.orderfood.config;

import com.dohung.orderfood.security.*;
import com.dohung.orderfood.security.SecurityUtils;
import com.dohung.orderfood.security.oauth2.AudienceValidator;
import com.dohung.orderfood.security.oauth2.JwtGrantedAuthorityConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import tech.jhipster.config.JHipsterProperties;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JHipsterProperties jHipsterProperties;

    @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}")
    private String issuerUri;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties, SecurityProblemSupport problemSupport) {
        this.problemSupport = problemSupport;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .disable()
            .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
        .and()
            .headers()
            .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
        .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
        .and()
            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; fullscreen 'self'; payment 'none'")
        .and()
            .frameOptions()
            .deny()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/auth-info").permitAll()


            .antMatchers(HttpMethod.GET,"/services/orderfood/api/menu/byNotRole").permitAll()
            .antMatchers(HttpMethod.GET,"/services/orderfood/api/menu/byWithRole").permitAll()
            .antMatchers(HttpMethod.GET,"/services/orderfood/api/menu/item-monan").permitAll()



            .antMatchers(HttpMethod.GET,"/services/orderfood/api/food/byFoodGroup/**").permitAll()
            .antMatchers(HttpMethod.GET,"/services/orderfood/api/food/foodDetail/**").permitAll()
            .antMatchers(HttpMethod.GET,"/services/orderfood/api/food/search/**").permitAll()

            .antMatchers(HttpMethod.GET,"/services/orderfood/api/countStar/**").permitAll()
            .antMatchers(HttpMethod.GET,"/services/orderfood/api/comment/**").permitAll()

            .antMatchers(HttpMethod.GET,"/services/orderfood/api/permission/get-notification/**").permitAll()
            .antMatchers(HttpMethod.POST,"/services/orderfood/api/notification/data").permitAll()

            .antMatchers(HttpMethod.GET,"/services/orderfood/api/permission/checknew/**").permitAll()

            .antMatchers(HttpMethod.POST,"/api/sayHello").permitAll()




            .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
//            .antMatchers("/api/**").authenticated()
            .antMatchers("/**").permitAll()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/health/**").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(authenticationConverter())
                .and()
            .and()
                .oauth2Client();
        // @formatter:on
    }

    Converter<Jwt, AbstractAuthenticationToken> authenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthorityConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(jHipsterProperties.getSecurity().getOauth2().getAudience());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper);
        return converter;
    }
}
