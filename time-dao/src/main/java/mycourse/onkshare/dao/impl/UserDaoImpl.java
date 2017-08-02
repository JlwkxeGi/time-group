package mycourse.onkshare.dao.impl;

import java.util.Collection;

import javax.annotation.Resource;

import mycourse.onkshare.model.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import mycourse.onkshare.dao.UserDao;

@Repository("userDao")
public class UserDaoImpl implements UserDao{

	@Resource(name="sqlSessionTemplate")
	SqlSessionTemplate template;

	@Override
	public Collection<User> findAllUsers() {
		/*UserMapper mapper = template.getMapper(UserMapper.class);
		Collection<User> users = mapper.selectAllUsers();
		return users;*/
		return null;
	}

	@Override
	public User findUserById(Long id) {
		/*UserMapper mapper = template.getMapper(UserMapper.class);
		User user = mapper.selectByPrimaryKey(id);
		return user;*/
		return null;
	}

}
