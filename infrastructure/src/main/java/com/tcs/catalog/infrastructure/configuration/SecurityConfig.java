package com.tcs.catalog.infrastructure.configuration;

import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Profile("!development")
public class SecurityConfig {

    private static final String ROLE_CATALOG_ADMIN = "CATALOG_ADMIN";
    private static final String ROLE_CATALOG_CAST_MEMBERS = "CATALOG_CAST_MEMBERS";
    private static final String ROLE_CATALOG_CATEGORIES = "CATALOG_CATEGORIES";
    private static final String ROLE_CATALOG_GENRES = "CATALOG_GENRES";
    private static final String ROLE_CATALOG_VIDEOS = "CATALOG_VIDEOS";

    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> {
                    csrf.disable();
                })
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/cast_members*").hasAnyRole(ROLE_CATALOG_ADMIN, ROLE_CATALOG_CAST_MEMBERS)
                            .requestMatchers("/categories*").hasAnyRole(ROLE_CATALOG_ADMIN, ROLE_CATALOG_CATEGORIES)
                            .requestMatchers("/genres*").hasAnyRole(ROLE_CATALOG_ADMIN, ROLE_CATALOG_GENRES)
                            .requestMatchers("/videos*").hasAnyRole(ROLE_CATALOG_ADMIN, ROLE_CATALOG_VIDEOS)
                            .anyRequest().hasRole(ROLE_CATALOG_ADMIN);
                })
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt(jwtConfigurer -> {
                        jwtConfigurer.jwtAuthenticationConverter(new KeycloakJwtConverter());
                    });
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .headers(headersConfigurer -> {
                    headersConfigurer.frameOptions(frameOptionsConfig -> {
                        frameOptionsConfig.sameOrigin();
                    });
                })
                .build();
    }

    static class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        private final KeycloakAuthoritiesConverter authoritiesConverter;

        public KeycloakJwtConverter() {
            this.authoritiesConverter = new KeycloakAuthoritiesConverter();
        }

        @Override
        public AbstractAuthenticationToken convert(final Jwt jwt) {

            return new JwtAuthenticationToken(jwt, extractAuthorities(jwt), extractPrincipal(jwt));
        }

        private String extractPrincipal(final Jwt jwt) {
            return jwt.getClaimAsString(JwtClaimNames.SUB);
        }

        private Collection<? extends GrantedAuthority> extractAuthorities(final Jwt jwt) {
            return authoritiesConverter.convert(jwt);
        }
    }

    static class KeycloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        private static final String REALM_ACCESS = "realm_access";
        private static final String ROLES = "roles";
        private static final String RESOURCE_ACCESS = "resource_access";
        public static final String ROLE_PREFIX = "ROLE_";

        @Override
        public Collection<GrantedAuthority> convert(final Jwt jwt) {
            final var realmRoles = extractRealmRoles(jwt);
            final var resourceRoles = extractResourceRoles(jwt);

            return Stream.concat(realmRoles, resourceRoles)
                    .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase()))
                    .collect(Collectors.toSet());
        }

        private Stream<String> extractResourceRoles(final Jwt jwt) {
            final Function<Map.Entry<String, Object>, Stream<String>> mapResource =
                    resource -> {
                        final var key = resource.getKey();
                        final var value = (JsonObject) resource.getValue();
                        final var roles = (Collection<String>) value.get(ROLES);
                        return roles.stream()
                                .map(role -> key.concat("_").concat(role));
                    };

            final Function<Set<Map.Entry<String, Object>>, Collection<String>> mapResources=
                    resources -> resources.stream()
                            .flatMap(mapResource)
                            .toList();


            return Optional.ofNullable(jwt.getClaimAsMap(RESOURCE_ACCESS))
                    .map(resources -> resources.entrySet())
                    .map(mapResources)
                    .orElse(Collections.emptyList())
                    .stream();

        }

        private Stream<String> extractRealmRoles(final Jwt jwt) {
            return Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS))
                    .map(resource -> (Collection<String>) resource.get(ROLES))
                    .orElse(Collections.emptyList())
                    .stream();

        }
    }
}
