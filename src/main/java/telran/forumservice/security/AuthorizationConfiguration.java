package telran.forumservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import lombok.RequiredArgsConstructor;
import telran.forumservice.accounting.dto.UserRoleEnum;

//@formatter:off

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration {

	final CustomWebSecurity customWebSecurity;

	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable()); //cross scripting filter
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)); //SESSION COOCKIES !!!!
		http.authorizeHttpRequests(authoraize -> authoraize

				.requestMatchers("/account/register", "/forum/posts/**")
				.permitAll()

				.requestMatchers("/account/user/{login}/role/{role}")
				.hasRole(UserRoleEnum.ADMINISTRATOR.name())

				.requestMatchers(HttpMethod.PUT, "/acoount/user/{login}")
				.access(new WebExpressionAuthorizationManager("#login == authentication.name"))

				.requestMatchers(HttpMethod.DELETE, "/acoount/user/{logn}")
				.access(new WebExpressionAuthorizationManager(
						"#login == authentication.name or hasRole('ADMINISTRATOR')"))

				.requestMatchers(HttpMethod.POST, "/forum/post/{author}")
				.access(new WebExpressionAuthorizationManager("#author == authentication.name"))

				.requestMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}")
				.access(new WebExpressionAuthorizationManager("#author == authentication.name"))

				.requestMatchers(HttpMethod.PUT, "/forum/post/{id}")
				.access((a, o) -> { 
					return customWebSecurity.checkPostAuthor(a, o);
					})

				.requestMatchers(HttpMethod.DELETE, "/forum/post/{id}")
				.access((a, o) -> {
					return customWebSecurity.customCheckBiFunction(a, o, (bar, foo) -> {
						return new AuthorizationDecision(
								o.getRequest().isUserInRole(UserRoleEnum.MODERATOR.name())
									|| customWebSecurity.checkPostAuthor(bar, foo).isGranted());
					});

				})
				 
				.anyRequest().authenticated());
		return http.build();
	}

}
