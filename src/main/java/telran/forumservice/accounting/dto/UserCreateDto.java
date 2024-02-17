package telran.forumservice.accounting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter

public class UserCreateDto {
	@Getter
	String login;
	String password;
	String firstName;
	String lastName;

}
