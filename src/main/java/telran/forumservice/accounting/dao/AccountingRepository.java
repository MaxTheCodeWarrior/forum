package telran.forumservice.accounting.dao;

import org.springframework.data.repository.CrudRepository;

import telran.forumservice.accounting.model.User;

public interface AccountingRepository extends CrudRepository<User, String> {


}
