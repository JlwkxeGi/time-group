package mycourse.onkshare.client.web.interceptor;

import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.model.User;
import mycourse.onkshare.stereotype.Permission;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.net.URLEncoder;

/**
 * Created by ww on 2017/1/31.
 */
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute(WebConstant.LOGINUSER);


        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            Permission permission = ((HandlerMethod) handler).getMethodAnnotation(Permission.class);
            String permissionValue = null;
            if (permission != null) {
                permissionValue = permission.value();
                System.out.print("角色"+permissionValue);
            }

            if (permissionValue == null || "".equals(permissionValue)) {
                return true;
            }else if (Permission.USER.equals(permissionValue)) {
                if (user == null || user.getId() == 0) {
                    httpSession.setAttribute("superUser", null);
                    String message = URLEncoder.encode("请先登录!!", "utf-8");
                    response.sendRedirect(WebConstant.BASEPATH +"/login?error=" + 2 + "&message=" + message  + "&basepath=" + WebConstant.BASEPATH);
                    return false;
                }
            }else if (Permission.SUPER_USER.equals(permissionValue)) {
                httpSession.setAttribute("superUser", null);
                String message = URLEncoder.encode("你没有权限!!", "utf-8");
                response.sendRedirect(WebConstant.BASEPATH +"/error?error=" + message + "&basepath=" + WebConstant.BASEPATH);
            }

        }else {
           /* if (user == null || user.getId() == 0) {
                httpSession.setAttribute("superUser", null);
                String message = URLEncoder.encode("请先登录!!", "utf-8");
                response.sendRedirect(WebConstant.BASEPATH +"/login?error=" + message + "&basepath=" + WebConstant.BASEPATH);
                return false;
            }*/
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
