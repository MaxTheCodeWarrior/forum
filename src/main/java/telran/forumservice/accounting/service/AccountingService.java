package telran.forumservice.accounting.service;

import telran.forumservice.accounting.dto.UserCreateDto;
import telran.forumservice.accounting.dto.UserDto;
import telran.forumservice.accounting.dto.UserRolesDto;
import telran.forumservice.accounting.dto.UserUpdateDto;

public interface AccountingService {

	UserDto registerUser(UserCreateDto userCreateDto); 

	UserDto loginUser(); // TODO

	UserDto deleteUser(String user);

	UserDto updateUser(String user, UserUpdateDto userUppdateDto);

	UserRolesDto addUserRole(String user, String role);

	UserRolesDto deleteUserRole(String user, String role);

	void changeUserPassword(String login, String newPassword);

	UserDto getUser(String user);

	UserDto getUserOrElseNull(String login);

}
