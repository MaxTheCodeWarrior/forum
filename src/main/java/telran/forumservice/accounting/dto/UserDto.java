package telran.forumservice.accounting.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	String login;

	String firstName;

	String lastName;

	Set<String> roles = new HashSet<>();

}
