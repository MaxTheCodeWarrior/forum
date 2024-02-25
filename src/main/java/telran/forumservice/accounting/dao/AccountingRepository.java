package telran.forumservice.accounting.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import telran.forumservice.accounting.model.UserAccount;

public interface AccountingRepository extends CrudRepository<UserAccount, String> {

	Optional<UserAccount> findByLogin(String login);

}
