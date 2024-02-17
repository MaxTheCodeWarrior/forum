package telran.forumservice.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class User {

	@Id
	String login;
	@Setter
	private String password;
	@Setter
	String firstName;
	@Setter
	String lastName;
	@Setter
	Set<String> roles = new HashSet<>();
}
