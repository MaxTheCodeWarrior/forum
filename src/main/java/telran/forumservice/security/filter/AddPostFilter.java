package telran.forumservice.security.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

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

@RequiredArgsConstructor
@Component
@Order(50)
public class AddPostFilter implements Filter {

	final AccountingRepository accountingRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			String[] pathParts = request.getServletPath().split("/");
			User user = accountingRepository.findById(pathParts[pathParts.length - 1]).orElse(null);
			if(user == null) {
				throw new HttpClientErrorException(HttpStatusCode.valueOf(401));
			}
			if (!request.getUserPrincipal().getName().equals(user.getLogin())) {
				response.sendError(403, "Permission denied");
				return;
			}
		}

		chain.doFilter(request, response);

	}

	private boolean checkEndPoint(String method, String path) {
		return HttpMethod.POST.matches(method) && path.startsWith("/forum/post/");
	}
}