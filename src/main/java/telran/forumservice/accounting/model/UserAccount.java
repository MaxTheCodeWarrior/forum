package telran.forumservice.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.forumservice.accounting.dto.UserRoleEnum;

@Getter
@NoArgsConstructor
@Setter
public class UserAccount {

	@Id
	String login;

	private String password;

	String firstName;

	String lastName;

	Set<UserRoleEnum> roles = new HashSet<>();
}
