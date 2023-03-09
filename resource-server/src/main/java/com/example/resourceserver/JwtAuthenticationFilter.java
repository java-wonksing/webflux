package com.example.resourceserver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    public static final String HEADER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = retrieveToken(exchange.getRequest());

        String jwkSetUri = "https://authclient-dev.spotvnow.co.kr/.well-known/jwks.json";
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        jwtDecoder.setJwtValidator(JwtValidators.createDefault());
        if (StringUtils.hasText(token)) {
            try {
                var jwtToken = jwtDecoder.decode(token);
                Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("all");
                User user = new User(jwtToken.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, token, authorities);
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }catch(JwtException e) {
                log.error(e.getMessage(), e);
                var res = exchange.getResponse();
//                res.getHeaders().set("www-authenticate", "xBasic");
                res.setStatusCode(HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private String retrieveToken(ServerHttpRequest request) {
        String idToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(idToken) && idToken.startsWith(HEADER_PREFIX)) {
            return idToken.substring(7);
        }
        var cookie = request.getCookies().getFirst("id_token");
        if (cookie != null){
            idToken = cookie.getValue();
        }
        log.info("id_token: %s", idToken);
        // test value
        return "eyJhbGciOiJSUzI1NiIsImtpZCI6ImMxNjMyZDkyLWIxMTEtNDJlMC1hYzJiLTk0ZTRhODc5OGRkZSIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2F1dGgtZGV2LnNwb3R2bm93LmNvLmtyIiwic3ViIjoiMTE5MTExMTEwMTA3MTE5IiwiYXVkIjpbIjM5NGZhOGM1LTRiMDAtNDU3NS05MjAzLTM3MDAyYTczZjFkMiJdLCJleHAiOjE2NzgzMjc1NTksImlhdCI6MTY3ODMyNzI1OSwianRpIjoiYmNiNTZmNmYtZmExMi00ZjIxLWEwZDYtNjA4NGY4ZTk5ZGJmIiwiYXpwIjoiIiwiZW1haWwiOiIiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZhbWlseV9uYW1lIjoiIiwiZ2l2ZW5fbmFtZSI6IiIsIm5hbWUiOiJOZXcgTmFtZSIsInRva2VuX3NvdXJjZSI6InNwb3R2bm93In0.i6A99YwQ-tdnAv6IVqJGDyX-b2S1WMhwc_jQOIAQd-V7XveEGjr639WNw6mEZV1uUlHJxoNZhoJc8g2rgGf78f3R7JJPSWseroSzVYBv5JQg4spRnI8a2B-07cU0XzVkk3azeGnRAvjlpHz_CaCdi2o5ipSkvp8WdDKwaXuVsO66nKQObGXRe4KAODdMmAvCsNr1WeHqlgTIHlnuJ8uRfrflXVq_3DrEEx8dazRYh3IjcRGX2yDo9dtuizLJKalS8Px675wdhZvDsacbOuA9czt3uRFjpuK4gm1xHaGQDf8TVaa8e0gj4tuYDZDVLCiIumtzuyAi4v-rVfMlu83mAPO4_JuXw9mGZDdpOn2uGUTRFR4Vdht_-yeVtbKQqBGt7iBjW55Yos99KZOpwQlJ8LgKHmoI1B6VODP-SqjZFQyn9Gx035UhfEPxk3Gu4YiH4I_obdtQOIdu2LSL6QoU01Z-iRf052L2QzezVc7PBtiEivAMgw7GygT6pOMl6li2i_TpjZ-EIWJuJDEZTCLHVNaNfd2BzDrCUQ42WR0lGhUlASlTHr92yuaPxFpnQonGuZhC3YOJdovHlbWLZ4xRSEuAWvtJfuSQbni0UCEGJEnP8PI_uiPKBgJY7lUrpRv-LZO1FuMZjgOrOklEfm5anOGB3CqnZRAqvJshKGAE97A";
//        return null
    }
}
