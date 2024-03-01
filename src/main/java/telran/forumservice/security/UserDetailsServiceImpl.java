package telran.forumservice.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.forumservice.accounting.dao.AccountingRepository;
import telran.forumservice.accounting.model.User;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	final AccountingRepository accountingRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = accountingRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
		Collection<String> authorities = user.getRoles().stream()
					.map(e -> "ROLE_" + e.name())
					.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
				AuthorityUtils.createAuthorityList(authorities));
	}

}
