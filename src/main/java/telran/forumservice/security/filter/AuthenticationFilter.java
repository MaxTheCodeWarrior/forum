package telran.forumservice.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.forumservice.accounting.dao.AccountingRepository;
import telran.forumservice.accounting.model.User;

@RequiredArgsConstructor
@Component
public class AuthenticationFilter implements Filter {

	final AccountingRepository accountingRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse responce = (HttpServletResponse) resp;

		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			User user;
			try {
				String[] credentials = getCredentials(request.getHeader("Authorization"));
				 user = accountingRepository.findById(credentials[0]).orElseThrow(RuntimeException::new);
				if (!BCrypt.checkpw(credentials[1], user.getPassword())) {
					throw new RuntimeException();
				}
			} catch (Exception e) {
				responce.sendError(401);
				return;
			}
			
			request = new WrappedRequest(request, user.getLogin());
			
		}
		
	
		
		chain.doFilter(request, responce);

	}

	private boolean checkEndPoint(String method, String path) {
		return !(HttpMethod.POST.matches(method) && path.equals("/account/register"));
	}

	private String[] getCredentials(String header) {
		String token = header.split(" ")[1];
		String decode = new String(Base64.getDecoder().decode(token));
		return decode.split(":");
	}
	
	private class WrappedRequest extends HttpServletRequestWrapper{
		private String login;
		
		public WrappedRequest(HttpServletRequest request, String login) {
			super(request);
			this.login = login;
		}
		@Override
		public Principal getUserPrincipal() {
			return () -> login;
		}
	}

}
