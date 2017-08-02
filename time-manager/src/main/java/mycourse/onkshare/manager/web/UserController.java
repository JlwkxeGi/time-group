package mycourse.onkshare.manager.web;

import static org.hamcrest.CoreMatchers.nullValue;

import mycourse.onkshare.common.util.JSONUtil;
import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.manager.service.UserService;
import mycourse.onkshare.stereotype.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("userController")
public class UserController extends BaseController{
	
	@Resource(name="userService")
	private UserService userService;


	@RequestMapping("/user/list")
	@ResponseBody
	public Result showUserList(@RequestParam int rows, @RequestParam int page) {
        PagingResult result = new PagingResult();
        result.setPageSize(rows);
        result.setPageNumber(page);
		userService.findAllUsers(result);
		return result;
	}
	
	@RequestMapping("/user/{id}")
	@ResponseBody
	public User showUser(@PathVariable(value="id") Long id) {
		User user = userService.findUserById(id);
		return user;
	}
	
	@RequestMapping("user/append")
	@ResponseBody
	public Result appendUser(@ModelAttribute User user) {
		//response.addHeader("Access-Control-Allow-Origin", "*");

		if(user == null) {
			Result result = Result.error("提交user为空添加失败!!");
			return result;
		}

		Map<String,String> errors = new HashMap<String, String>();

		if (user.getUsername() == null || user.getUsername().isEmpty()) {
			errors.put("user.username", "username为空");
		}
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			errors.put("user.password", "password无效");
		}
		if(user.getStatus() == null) {
			byte b = 0;
			user.setStatus(b);
		}
		if(user.getPhone() == null || user.getPhone().isEmpty()) {
			user.setPhone(null);
		}
		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			user.setEmail(null);
		}

		if(!errors.isEmpty()) {
			Result result = Result.error(JSONUtil.mapToJSON(errors));
			return result;
		}

		Result result = userService.addUser(user);
		return result;
	}
	
	@RequestMapping("user/update")
	@ResponseBody
	public Result updateUser(@ModelAttribute User user) {
		if(user == null) {
			Result result = Result.error("提交user为空更新失败!!");
			return result;
		}

		Map<String,String> errors = new HashMap<String,String>();

		if (user.getUsername() == null || user.getUsername().isEmpty()) {
			errors.put("user.username", "username为空");
		}
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			errors.put("user.password", "password无效");
		}
		
		if(!errors.isEmpty()) {
			Result result = Result.error(JSONUtil.mapToJSON(errors));
			return result;
		}

		Result result = userService.updateUser(user);
		
		return result;
	}

	@RequestMapping("user/remove")
	@ResponseBody
	public Result removeUser(@RequestParam(value = "ids", required = true, defaultValue = "")  String ids) {

		String[] split = ids.split(",");
		List<Long> list = new ArrayList();
		for (String sId: split) {
			if (sId != null && !"".equals(sId) && !"undefined".equals(sId)) {
				Long aLong = Long.valueOf(sId);
				list.add(aLong);
			}
		}
		Result result = userService.removeUser(list,getSessionUser());
		return result;
	}

	@RequestMapping("/user/login")
	@ResponseBody
	public ModelAndView login(String username, String password) {
		Map<String,String> errors = new HashMap<String, String>();
		ModelAndView view = new ModelAndView();

		if (username == null || username.isEmpty()) {
			errors.put("user.username", "username为空");
		}
		if (password == null ||password.isEmpty()) {
			errors.put("user.password", "password无效");
		}
		if(!errors.isEmpty()) {
			return view.addAllObjects(errors);
		}

		User user = null;
		try {
			user = userService.login(username, password);
		} catch (RuntimeException e) {
			return view.addObject("error", 1).addObject("message", e.getMessage());
		}finally {
			if (user == null) {
				return  view.addObject("error", 1).addObject("message", "登录失败!!");
			}
		}
		boolean isSuper = confirmAdmin(user);
		user.setSuper(isSuper);
		request.getSession().setAttribute(WebConstant.LOGINUSER, user);
		view.setViewName("redirect:/manager/index");
		return view.addObject("error", 0).addObject("message", "登录成功!!");
	}


	@RequestMapping("/user/logout")
	@ResponseBody
	@Permission(Permission.USER)
	public Result logout() {
		HttpSession session = request.getSession();
		session.removeAttribute(WebConstant.LOGINUSER);
		return Result.SUCCESS;
	}

	@RequestMapping("/user/register")
	@ResponseBody
	public ModelAndView register(String username , String password, String confirm) {
		Map<String,String> errors = new HashMap<String, String>();
		ModelAndView view = new ModelAndView();

		if (username == null || username.isEmpty()) {
			errors.put("username", "username为空");
		}
		if (password == null ||password.isEmpty()) {
			errors.put("password", "password无效");
		}
		if (confirm == null ||confirm.isEmpty()) {
			errors.put("confirm", "confirm无效");
		}
		if(!confirm.equals(password)) {
			errors.put("password", "两次密码不同");
		}
		if(!errors.isEmpty()) {
			view.addAllObjects(errors);
			return view;
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		Result result = userService.addUser(user);
		if (result.getError() == 1 ) {
			return view.addObject("error", 1).addObject("message", result.getMessage());
		}
		view.setViewName("redirect:/manager/login");
		return view.addObject("error", 0).addObject("message", "注册成功!!");
	}

	protected boolean confirmAdmin(User user) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			URL url = User.class.getClassLoader().getResource("manager-user.xml");
			Document parse = builder.parse(url.getPath());
			NodeList users = parse.getElementsByTagName("user");
			for (int i = 0; i < users.getLength(); i++) {
				Node item = users.item(i);
				if (user.getUsername().equals(item.getTextContent())) {
					return true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
