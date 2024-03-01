package telran.forumservice.security;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.forumservice.dao.ForumRepository;
import telran.forumservice.model.Post;

@Service("customSecurity")
@RequiredArgsConstructor

public class CustomWebSecurity { 

	private final ForumRepository forumRepository;

	public AuthorizationDecision checkPostAuthor(Supplier<Authentication> a,
			RequestAuthorizationContext o) {
		String[] path = o.getRequest().getServletPath().split("/");
			String id = path[path.length - 1];
				Post post = forumRepository.findById(id).orElse(null);
					return new AuthorizationDecision(post != null &&
							a.get().getName().equals(post.getAuthor()));

	} 

	public AuthorizationDecision customCheckBiFunction(Supplier<Authentication> a, RequestAuthorizationContext o,
			BiFunction<Supplier<Authentication>, RequestAuthorizationContext, AuthorizationDecision> foo) {
				return foo.apply(a, o);
	}

	
	/*
	 * write your custom check methods below 
	 * or implement AuthorizationManager<RequestAuthorizationContext> with default method check
	 */
	
}
