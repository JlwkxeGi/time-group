package mycourse.onkshare.manager.web.interceptor;

import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.model.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

/**
 * Created by ww on 2017/1/31.
 */
public class BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
            验证user
         */
        /*String url = request.getRequestURI();
        String login  = WebConstant.BASEPATH + "/user/login";
        String loginPage  = WebConstant.BASEPATH + "/login";
        String regist = WebConstant.BASEPATH +"/user/regist";
        String registPage = WebConstant.BASEPATH +"/regist";*/

        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute(WebConstant.LOGINUSER);

        /*if (login.equals(url) || loginPage.equals(url)) {
            return true;
        }
        if (regist.equals(url) || registPage.equals(url)) {
            return true;
        }*/
        if (user == null || user.getId() == 0) {
            httpSession.setAttribute("superUser", null);
            String message = URLEncoder.encode("请先登录!!", "utf-8");
            response.sendRedirect(WebConstant.BASEPATH +"/login?error=" + message + "&basepath=" + WebConstant.BASEPATH);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        if (mv != null) {
            HttpSession session = request.getSession();
            mv.addObject("basepath", WebConstant.BASEPATH);
            mv.addObject("csspath", WebConstant.CSSPATH);
            mv.addObject("imageurl", WebConstant.IMAGEPATH);
            if(session.getAttribute(WebConstant.LOGINUSER) == null) {
                return;
            }
            mv.addObject(WebConstant.LOGINUSER, session.getAttribute(WebConstant.LOGINUSER));
    //		mv.addObject("child", getLoginUserMenu());
    //		mv.addObject("menu", getLoginUserMenu2());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    /*private void log(Object handler, ModelAndView vm) {
        if (!(handler instanceof HandlerMethod)) {
            return ;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        String viewname = vm.getViewName();
        if(viewname != null) {
            logger.debug("请求服务log: " + method.getName() + "-->>" +vm.getViewName());
        }
    }*/

}
