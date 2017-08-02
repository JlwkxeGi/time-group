package mycourse.onkshare.client.web.interceptor;

import mycourse.onkshare.common.util.JSONUtil;
import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.Result;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * Created by ww on 2017/1/31.
 */
public class BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

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
