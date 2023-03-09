package com.example.resourceserver;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@RequiredArgsConstructor
@RestController
public class SecurityConfig {
//    public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//
//        @Override
//        public void onAuthenticationFailure(ServerHttpRequest httpServletRequest, ServerHttpResponse httpServletResponse, AuthenticationException e) throws IOException {
//            httpServletResponse.sendRedirect("/customError");
//        }
//    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
//        return http.build();

//        return http.headers().frameOptions().disable().and()
//                .csrf().disable()
//                .httpBasic().disable()
//                .formLogin().disable()
//                .logout().disable()
//                .authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
//                .build();

        return http.headers().frameOptions().disable().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
                .addFilterAt(new JwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();

    }
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefault();
        String jwkSetUri = "https://authclient-dev.spotvnow.co.kr/.well-known/jwks.json";
        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
        jwtDecoder.setJwtValidator(jwtValidator);
        return jwtDecoder;
    }

//    private AuthenticationWebFilter authenticationWebFilter() {
//        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager());
//
//        filter.setRequiresAuthenticationMatcher(LOGIN_MATCHER);
//        filter.setSecurityContextRepository(securityContextRepository());
//        filter.setServerAuthenticationConverter(new CustomServerAuthenticationConverter());
//        filter.setAuthenticationSuccessHandler(new CustomServerAuthenticationSuccessHandler());
//        filter.setAuthenticationFailureHandler(new CustomServerAuthenticationFailureHandler());
//
//        return filter;
//    }








//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/dummy/**").hasAuthority("SCOPE_message:read")
//                        .anyExchange().authenticated()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(Customizer.withDefaults())
//                );
//        return http.build();
//    }
//    @Bean
//    public SecurityWebFilterChain securityWebFiltersOrder(ServerHttpSecurity httpSecurity) {
//        return httpSecurity
//                .headers().frameOptions().disable().and()
//                .csrf().disable()
//                .httpBasic().disable().formLogin()
//                .disable().logout().disable()
//                .securityContextRepository(securityContextRepository())
//                .exceptionHandling()
//                .authenticationEntryPoint(new ServerAuthenticationEntryPoint() {
//                    @Override
//                    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
//                        exchange.getResponse().getHeaders().set("www-authenticate", "xBasic");
//                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                        return Mono.empty();
//                    }
//                }).and()
//                .authorizeExchange().anyExchange().permitAll().and()
//                .addFilterAt(this.isTest ? new ExchangeBodyLogFilter2() : new ExchangeBodyLogFilter(), SecurityWebFiltersOrder.FIRST)
//                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
//                .addFilterAt(stvAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
//                .addFilterAt(logoutWebFilter(), SecurityWebFiltersOrder.LOGOUT)
//                .addFilterAt(stvLogoutWebFilter(), SecurityWebFiltersOrder.LOGOUT)
//                .build();
//    }
}
