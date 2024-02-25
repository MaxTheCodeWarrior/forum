package telran.forumservice.accounting.service;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.forumservice.accounting.dao.AccountingRepository;
import telran.forumservice.accounting.dto.UserCreateDto;
import telran.forumservice.accounting.dto.UserDto;
import telran.forumservice.accounting.dto.UserRoleEnum;
import telran.forumservice.accounting.dto.UserRolesDto;
import telran.forumservice.accounting.dto.UserUpdateDto;
import telran.forumservice.accounting.model.UserAccount;
import telran.forumservice.exceptions.UserExistsException;
import telran.forumservice.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService, CommandLineRunner {

	final AccountingRepository accountRepository;
	final ModelMapper modelMapper;

	@Override
	public UserDto registerUser(UserCreateDto userCreateDto) {
		if (accountRepository.existsById(userCreateDto.getLogin())) {
			throw new UserExistsException();
		}
		UserAccount user = modelMapper.map(userCreateDto, UserAccount.class);
		String password = BCrypt.hashpw(userCreateDto.getPassword(), BCrypt.gensalt());
		user.setPassword(password);
		user.getRoles().add(UserRoleEnum.USER);
		accountRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto deleteUser(String login) {
		UserAccount user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		accountRepository.delete(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserUpdateDto userUppdateDto) {
		UserAccount user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		user.setFirstName(userUppdateDto.getFirstName());
		user.setLastName(userUppdateDto.getLastName());
		accountRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserRolesDto addUserRole(String login, String role) {
		UserAccount user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().add(UserRoleEnum.valueOf(role.toUpperCase()));
		accountRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public UserRolesDto deleteUserRole(String login, String role) {
		UserAccount user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().remove(UserRoleEnum.valueOf(role.toUpperCase()));
		accountRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public void changeUserPassword(String login, String newPassword) {
		UserAccount user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		user.setPassword(password);
		accountRepository.save(user);
	}

	@Override
	public UserDto getUser(String login) {
		UserAccount user = accountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user, UserDto.class);
	}

	/* @formatter:off */
	@Override
	public void run(String... args) throws Exception {
		if(!accountRepository.existsById("admin")) {
			UserAccount user = new UserAccount();
				user.setLogin("admin");
					user.setFirstName("");
						user.setLastName("");
							user.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
								user.getRoles().add(UserRoleEnum.MODERATOR);
									user.getRoles().add(UserRoleEnum.ADMINISTRATOR);
										accountRepository.save(user);
		}
		
	}

}
