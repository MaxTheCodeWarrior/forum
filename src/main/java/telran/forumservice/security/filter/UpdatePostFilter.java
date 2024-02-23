package telran.forumservice.security.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.forumservice.accounting.dao.AccountingRepository;
import telran.forumservice.accounting.model.User;
import telran.forumservice.dao.ForumRepository;
import telran.forumservice.exceptions.PostNotFoundException;
import telran.forumservice.model.Post;

@RequiredArgsConstructor
@Component
@Order(50)
public class UpdatePostFilter implements Filter {

	final AccountingRepository accountingRepository;

	final ForumRepository forumRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		if (checkEndPoint(request.getMethod(), request.getServletPath())) {

			User user = accountingRepository.findById(request.getUserPrincipal().getName()).get();

			String[] pathParts = request.getServletPath().split("/");

			Post post = forumRepository.findById(pathParts[pathParts.length - 1])
					.orElseThrow(PostNotFoundException::new);

			if (!post.getAuthor().equals(user.getLogin())) {
				response.sendError(403, "Permission denied");
				return;
			}
		}

		chain.doFilter(request, response);

	}


	private boolean checkEndPoint(String method, String path) {
		return HttpMethod.PUT.matches(method) && ( path.startsWith("/forum/post/") 
							&& !path.contains("/comment") && !path.contains("/like")
							);
	}
}