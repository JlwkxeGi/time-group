package mycourse.onkshare.manager.service;


import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.model.User;

import java.util.List;

public interface UserService {

	Result addUser(User user);
	
	Result updateUser(User user);

	Result removeUser(List<Long> ids, User currentUser);

	User findUserById(Long id);

	User findUserByName(String name);

	PagingResult findAllUsers(PagingResult result);

	User login(String username, String password);

}
