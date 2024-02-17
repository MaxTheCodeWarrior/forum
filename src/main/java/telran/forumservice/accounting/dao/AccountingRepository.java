package telran.forumservice.accounting.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import telran.forumservice.accounting.model.User;

public interface AccountingRepository extends CrudRepository<User, String> {

	Optional<User> findByLogin(String login);

}
