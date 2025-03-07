package dev.jartur.oitava.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorizationFilter extends OncePerRequestFilter {

	private List<String> openRoutes = Arrays.asList("/signup", "/signin");

	@SuppressWarnings("null")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (openRoutes.contains(request.getServletPath())) {
			filterChain.doFilter(request, response);
		} else {
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					String accessToken = authorizationHeader.substring("Bearer ".length());

					Algorithm algorithm = Algorithm.HMAC256(Base64.getDecoder().decode(SecurityConfig.JWT_SECRET));
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(accessToken);

					String email = decodedJWT.getSubject();

					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
							.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

					var authenticationToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);

					filterChain.doFilter(request, response);
				} catch (Exception e) {
					e.printStackTrace();

					Map<String, String> error = new HashMap<>();
					error.put("error", e.getMessage());

					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					response.setStatus(HttpStatus.FORBIDDEN.value());
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}

}
