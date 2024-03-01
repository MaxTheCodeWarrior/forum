package telran.forumservice.accounting.service;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.forumservice.accounting.dao.AccountingRepository;
import telran.forumservice.accounting.dto.UserCreateDto;
import telran.forumservice.accounting.dto.UserDto;
import telran.forumservice.accounting.dto.UserRoleEnum;
import telran.forumservice.accounting.dto.UserRolesDto;
import telran.forumservice.accounting.dto.UserUpdateDto;
import telran.forumservice.accounting.model.User;
import telran.forumservice.exceptions.UserExistsException;
import telran.forumservice.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService, CommandLineRunner {

	final AccountingRepository accountRepository;
	final ModelMapper modelMapper;
	final PasswordEncoder passwordEncoder;

	@Override
	public UserDto registerUser(UserCreateDto userCreateDto) {
		if (accountRepository.existsById(userCreateDto.getLogin())) {
			throw new UserExistsException();
		}
		User user = modelMapper.map(userCreateDto, User.class);
		String password = passwordEncoder.encode(userCreateDto.getPassword());
		user.setPassword(password);
		user.getRoles().add(UserRoleEnum.USER);
		accountRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto deleteUser(String login) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		accountRepository.delete(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserUpdateDto userUppdateDto) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.setFirstName(userUppdateDto.getFirstName());
		user.setLastName(userUppdateDto.getLastName());
		accountRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserRolesDto addUserRole(String login, String role) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().add(UserRoleEnum.valueOf(role.toUpperCase()));
		accountRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public UserRolesDto deleteUserRole(String login, String role) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().remove(UserRoleEnum.valueOf(role.toUpperCase()));
		accountRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public void changeUserPassword(String login, String newPassword) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		String password = passwordEncoder.encode(newPassword);
		user.setPassword(password);
		accountRepository.save(user);
	}

	@Override
	public UserDto getUser(String login) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user, UserDto.class);
	}

	/* @formatter:off */
	
	//CommandLineRunner 
	@Override
	public void run(String... args) throws Exception {
		if(!accountRepository.existsById("admin")) {
			User user = new User();
				user.setLogin("admin");
					user.setFirstName("");
						user.setLastName("");
							user.setPassword(passwordEncoder.encode("admin"));
								user.getRoles().add(UserRoleEnum.MODERATOR);
									user.getRoles().add(UserRoleEnum.ADMINISTRATOR);
										accountRepository.save(user);
		}
		
	}

}
