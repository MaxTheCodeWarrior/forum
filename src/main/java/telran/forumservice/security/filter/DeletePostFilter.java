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
import telran.forumservice.accounting.dto.UserRoleEnum;
import telran.forumservice.dao.ForumRepository;
import telran.forumservice.model.Post;
import telran.forumservice.security.model.User;

@RequiredArgsConstructor
@Component
@Order(50)
public class DeletePostFilter implements Filter {

	final ForumRepository forumRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			User user = (User) request.getUserPrincipal();

			String[] arr = request.getServletPath().split("/");
			String postId = arr[arr.length - 1];
			Post post = forumRepository.findById(postId).orElse(null);
			if (post == null) {
				response.sendError(404);
				return;
			}
			if (!(user.getName().equals(post.getAuthor())
					|| user.getRoles().contains(UserRoleEnum.MODERATOR.getValue()))) {
				response.sendError(403);
				return;
			}

		}

		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		return HttpMethod.DELETE.matches(method) && path.matches("/forum/post/\\w+");

	}
}
