package mycourse.onkshare.manager.service.impl;


import javax.annotation.Resource;

import mycourse.onkshare.exception.CustomRuntimeException;
import mycourse.onkshare.manager.service.UserService;
import mycourse.onkshare.mapper.FileDescMapper;
import mycourse.onkshare.mapper.FileMapper;
import mycourse.onkshare.mapper.UserMapper;
import mycourse.onkshare.model.*;
import mycourse.onkshare.model.result.MessageResult;
import mycourse.onkshare.model.result.ObjectResult;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	@Resource
	private FileMapper fileMapper;
	@Resource
	private FileDescMapper fileDescMapper;
	
	@Override
	public PagingResult findAllUsers(PagingResult result) {
		Map<String, Object> map = new HashMap();
		map.put("startPoint",(result.getPageNumber()-1)*result.getPageSize());
		map.put("length",result.getPageSize());

		List<User> users = userMapper.selectFileByLimit(map);

		result.setRows(users);
		result.setTotal(userMapper.countByExample(null));
		return result;
	}

	@Override
	public User findUserById(Long id) {
		if (id == null) {
			throw new RuntimeException(this.getClass() + ".findUserbyId()" + ": id 为空");
		}
		User user = userMapper.selectByPrimaryKey(id);
		return user;
	}

	@Override
	public User findUserByName(String name) {
		if (name == null || "".equals(name.trim())) {
			throw new RuntimeException(this.getClass() + ".findUserbyId()" + ": id 为空");
		}
		User user = userMapper.selectUserByName(name);
		return user;
	}

	@Override
	public Result addUser(User user) {
		/*
			添加用户的附加信息
		 */
		user.setCreated(new Date());
		user.setUpdated(new Date());

		/*
			添加
		 */
		int insert = 0;
		try {
			insert = userMapper.insert(user);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(insert == 0) {
				Result result = Result.error("添加失败!!");
				return result;
			}
		}

		/*
			分配文件用户文件夹
		 */
		java.io.File path = new java.io.File("D:/temp/onkshare/" + user.getUsername());
		if (!path.exists()) {
			path.mkdir();
		}
		User userByName = findUserByName(user.getUsername());
		File file = new File();
		file.setName(userByName.getUsername());
		file.setParentId(0L);
		file.setCreated(new Date());
		file.setUpdated(new Date());
		file.setUserId(userByName.getId());
		file.setChildren(0);
		file.setStorageId("folder");
		file.setStorageMethod("folder");
		file.setUrl("");
		fileMapper.insert(file);

		ObjectResult result = new ObjectResult();
		result.setObject(userByName);
		result.setMessage("success");
		return result;
	}

	@Override
	public Result updateUser(User user) {
		/*
			验证用户是否存在
		 */
		User selectUser = userMapper.selectByPrimaryKey(user.getId());
		if(selectUser == null) {
			Result error = Result.error("更新用户不存在!!");
			return error;
		}
		/*
			填充用户附加信息
		 */
		if (user.getStatus() == null) {
			user.setStatus(selectUser.getStatus());
		}
		if (user.getEmail() == null) {
			user.setEmail(selectUser.getEmail());
		}
		if (user.getPassword() == null) {
			user.setPassword(selectUser.getPassword());
		}
		if (user.getPhone() == null) {
			user.setPhone(selectUser.getPhone());
		}
		user.setCreated(selectUser.getCreated());
		user.setUpdated(new Date());
		/*
			更新
		 */
		int key = userMapper.updateByPrimaryKey(user);
		if(key == 0) {
			Result error = Result.error("更新失败!!");
			return error;
		}
		ObjectResult result = new ObjectResult();
		result.setObject(user);
		result.setMessage("success");
		return result;
	}

	@Override
	public Result removeUser(List<Long> ids, User currentUser) {

		if (!currentUser.isSuper()) {
			throw new CustomRuntimeException("没有权限!!");
		}
		UserExample userExample = new UserExample();
		UserExample.Criteria criteria = userExample.createCriteria();
		criteria.andIdIn(ids);
		int key = userMapper.deleteByExample(userExample);
		if(key == 0) {
			Result error = Result.error("删除失败!!");
			return error;
		}
		/*
			删除文件的详情
		 */
		FileExample fileExample = new FileExample();
		FileExample.Criteria fileCriteria = fileExample.createCriteria();
		fileCriteria.andUserIdIn(ids);
		List<File> files = fileMapper.selectByExample(fileExample);
		FileDescExample fileDescExample = new FileDescExample();
		FileDescExample.Criteria fileDescCriteria = fileDescExample.createCriteria();
		List<Long> longs = new ArrayList<>();
		for (File file : files) {
			longs.add(file.getId());
		}
		fileDescCriteria.andFileIdIn(longs);
		fileDescMapper.deleteByExample(fileDescExample);

		/*
			删除user文件相关文件
		 */
		fileMapper.deleteByExample(fileExample);

		return MessageResult.SUCCESS;
	}

	@Override
	public User login(String username, String password) {
		User user = null;
		try {
			user = userMapper.selectUserByName(username);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (user == null) {
				throw  new RuntimeException("用户不存在!!");
			}
			if (!password.equals(user.getPassword())) {
				throw  new RuntimeException("密码错误!!");
			}
			user.setPassword("");
			return user;
		}
	}
}
