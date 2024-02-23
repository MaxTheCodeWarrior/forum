package telran.forumservice.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class User {

	@Id
	String login;

	private String password;

	String firstName;

	String lastName;

	Set<String> roles = new HashSet<>();
}
