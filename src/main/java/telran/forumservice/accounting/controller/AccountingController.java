package telran.forumservice.accounting.controller;

import java.security.Principal;
import java.util.Base64;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import telran.forumservice.accounting.dto.UserCreateDto;
import telran.forumservice.accounting.dto.UserDto;
import telran.forumservice.accounting.dto.UserRolesDto;
import telran.forumservice.accounting.dto.UserUpdateDto;
import telran.forumservice.accounting.service.AccountingService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account") // {{baseUrl}}
public class AccountingController {

	final AccountingService accountingService;
	final ModelMapper modelMapper;

	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserCreateDto userCreateDto) {
		if (accountingService.getUserOrElseNull(userCreateDto.getLogin()) == null) {
			UserDto userDto = accountingService.registerUser(userCreateDto);
			accountingService.registerUser(userCreateDto);
			return ResponseEntity.status(HttpStatus.OK).body(userDto);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@PostMapping("/login")
	public ResponseEntity<UserDto> loginUser(Principal principal) {
//		token = token.split(" ")[1];
//		String credentials = new String(Base64.getDecoder().decode(token));

		// TODO Not implemented loginUser() method
		// credentials.split(":")[0])
		return ResponseEntity.status(HttpStatus.OK).body(accountingService.getUser(principal.getName()));
	}

	@DeleteMapping("/user/{user}")
	public ResponseEntity<UserDto> deleteUser(@PathVariable String user) {
		UserDto userDto = accountingService.deleteUser(user);
		return ResponseEntity.status(userDto != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(userDto);
	}

	@PutMapping("/user/{user}")
	public ResponseEntity<UserDto> updateUser(@PathVariable String user, @RequestBody UserUpdateDto userUppdateDto) {
		UserDto userDto = accountingService.updateUser(user, userUppdateDto);
		return ResponseEntity.status(userDto != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(userDto);
	}

	@PutMapping("/user/{user}/role/{role}")
	public ResponseEntity<UserRolesDto> addUserRole(@PathVariable String user, @PathVariable String role) {
		UserRolesDto userRolesDto = accountingService.addUserRole(user, role);
		return ResponseEntity.status(userRolesDto != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)
				.body(userRolesDto);

	}

	@DeleteMapping("/user/{user}/role/{role}")
	public ResponseEntity<UserRolesDto> deleteUserRole(@PathVariable String user, @PathVariable String role) {
		UserRolesDto userRolesDto = accountingService.deleteUserRole(user, role);
		return ResponseEntity.status(userRolesDto != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)
				.body(userRolesDto);
	}

	@PutMapping("/password")
	public ResponseEntity<Void> changeUserPassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
		accountingService.changeUserPassword(principal.getName(), newPassword);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

	@GetMapping("/user/{user}")
	public ResponseEntity<UserDto> getUser(@PathVariable String user) {
		UserDto userDto = modelMapper.map(accountingService.getUser(user), UserDto.class);
		return ResponseEntity.status(userDto != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(userDto);

	}

}
