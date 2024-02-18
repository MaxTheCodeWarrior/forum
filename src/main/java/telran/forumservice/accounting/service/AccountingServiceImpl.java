package telran.forumservice.accounting.service;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.forumservice.accounting.dao.AccountingRepository;
import telran.forumservice.accounting.dto.UserCreateDto;
import telran.forumservice.accounting.dto.UserDto;
import telran.forumservice.accounting.dto.UserRolesDto;
import telran.forumservice.accounting.dto.UserUpdateDto;
import telran.forumservice.accounting.model.User;
import telran.forumservice.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService {

	final AccountingRepository accountRepository;
	final ModelMapper modelMapper;

	@Override
	public UserDto registerUser(UserCreateDto userCreateDto) {
		UserDto userDto = modelMapper.map(userCreateDto, UserDto.class);
		userDto.getRoles().add("USER"); 
		String password = BCrypt.hashpw(userCreateDto.getPassword(), BCrypt.gensalt());
		User user = modelMapper.map(userDto, User.class);
		user.setPassword(password);
		accountRepository.save(user);
		return userDto;
	}

	@Override
	public UserDto loginUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto deleteUser(String login) {
		User user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		accountRepository.delete(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserUpdateDto userUppdateDto) {
		User user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		user.setFirstName(userUppdateDto.getFirstName());
		user.setLastName(userUppdateDto.getLastName());
		accountRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserRolesDto addUserRole(String login, String role) {
		User user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().add(role);
		accountRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public UserRolesDto deleteUserRole(String login, String role) {
		User user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().remove(role);
		accountRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public void changeUserPassword(String login, String newPassword) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.setPassword(newPassword);
		accountRepository.save(user);
	}

	@Override
	public UserDto getUserOrElseNull(String login) {
		User user = accountRepository.findByLogin(login).orElse(null);
		return user != null ? modelMapper.map(user, UserDto.class) : null;

	}

	@Override
	public UserDto getUser(String login) {
		User user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user, UserDto.class);
	}

}
