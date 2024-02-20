package telran.forumservice.accounting.service;

import telran.forumservice.accounting.dto.UserCreateDto;
import telran.forumservice.accounting.dto.UserDto;
import telran.forumservice.accounting.dto.UserRolesDto;
import telran.forumservice.accounting.dto.UserUpdateDto;

public interface AccountingService {

	UserDto registerUser(UserCreateDto userCreateDto); 

	UserDto deleteUser(String login);

	UserDto updateUser(String login, UserUpdateDto userUppdateDto);

	UserRolesDto addUserRole(String login, String role);

	UserRolesDto deleteUserRole(String login, String role);

	void changeUserPassword(String login, String newPassword);

	UserDto getUser(String login);


}
