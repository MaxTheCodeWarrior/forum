package telran.forumservice.security.filter;

import java.io.IOException;
import java.util.Arrays;

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
import telran.forumservice.accounting.model.UserAccount;
import telran.forumservice.exceptions.UserNotFoundException;

@RequiredArgsConstructor
@Component
@Order(30)
public class UpdateByOwnerFilter implements Filter {

	final AccountingRepository accountingRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		if (checkEndPoint(request.getMethod(), request.getServletPath())) {

			UserAccount user = accountingRepository.findById(request.getUserPrincipal().getName())
														.orElseThrow(UserNotFoundException::new);
			String[] pathParts = request.getServletPath().split("/");

			if (!Arrays.asList(pathParts).contains(user.getLogin())) {
				response.sendError(403, "Permission denied");
				return;
			}
		}

		chain.doFilter(request, response);

	}

	private boolean checkEndPoint(String method, String path) {

		return HttpMethod.PUT.matches(method) && path.startsWith("/account/user/");
	}
}
