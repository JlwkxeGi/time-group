package mycourse.onkshare.api.web;

import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 *
 */
@Controller
public class BaseController {

	public Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;


	public boolean confirmUserLife(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		User user = (User) httpSession.getAttribute(WebConstant.LOGINUSER);
		if (user != null && user.getId() != 0) {
			return true;
		}
		return false;
	}

	protected User getSessionUser() {
		User user = (User) request.getSession().getAttribute(WebConstant.LOGINUSER);
		return user;
	}

	protected boolean confirmAdmin() {
		User user = (User) request.getSession().getAttribute(WebConstant.LOGINUSER);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parse = builder.parse(ClassLoader.getSystemResource("manager-user.xml").getPath());
			NodeList users = parse.getElementsByTagName("user");
			for (int i = 0; i < users.getLength(); i++) {
				Node item = users.item(i);
				if (item.getNodeValue().equals(user.getUsername())) {
					return true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}


	private void inflateLog(Object proceed, ProceedingJoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().toShortString();
		/*
			requestLog
		 */
		logger.debug("当前请求: " + request.getRequestURL().toString());
		/*
		 * commonLog
		 */
		if(proceed == null) {
			logger.debug("请求服务log: " + methodName + "-->>return null");
			return;
		}
		if(proceed instanceof Result) {
			Result result = (Result) proceed;
			logger.debug("请求服务log: " + methodName + "-->>return Result :" +result.getMessage());
			return;
		}
		if(proceed instanceof  String) {
			String result = (String)proceed;
			logger.debug("请求服务log: " + methodName + "-->>return String :" +result);
			return;
		}
		if(proceed instanceof ModelAndView) {
			ModelAndView mv = (ModelAndView)proceed;
			logger.debug("请求服务log: " + methodName + "-->>return ModelAndView :" +mv.getViewName());
			return;
		}
		logger.debug("请求服务log: " + methodName + "-->>return default!");
	}


	/**
	 * aop
	 * @param joinpoint
	 * @return
	 */
	protected Object arround(ProceedingJoinPoint joinpoint) {

		try {

			/*
				验证user
		 	*/
			/*String url = request.getRequestURI();
			String login  = WebConstant.BASEPATH + "/user/login";
			String loginPage  = WebConstant.BASEPATH + "/login";
			String regist = WebConstant.BASEPATH +"/user/regist";
			String registPage = WebConstant.BASEPATH +"/regist";
			if (login.equals(url) || loginPage.equals(url)) {

			}else if (regist.equals(url) || registPage.equals(url)) {

			}else if (!confirmUserLife(request)) {
				String message = URLEncoder.encode("请先登录!!", "utf-8");
				response.sendRedirect(WebConstant.BASEPATH +"/login?error=" + message);
			}*/


			/*
			 * process
			 */

			Object[] args = joinpoint.getArgs();
			Object proceed = joinpoint.proceed(args);
			/*
				填充Constant
			 */
			//inflateCommonView(proceed, joinpoint);

			/*
				填充log
			 */
			inflateLog(proceed, joinpoint);
			
			return proceed;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void before() throws Exception{

	}
}
