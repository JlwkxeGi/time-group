package mycourse.onkshare.dao;

import mycourse.onkshare.model.User;

import java.util.Collection;

public interface UserDao {
	
	public Collection<User> findAllUsers();

	public User findUserById(Long id);
}
