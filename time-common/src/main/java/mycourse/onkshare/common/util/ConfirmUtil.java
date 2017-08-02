package mycourse.onkshare.common.util;

import mycourse.onkshare.model.User;

/**
 * Created by doll on 2017/2/20.
 */
public class ConfirmUtil {

    /**
     *  判断用户的权限
     * @param confirm  需要验证的用户
     * @param template
     * @return 如果返回true, 说明两用户是同一用户或当前用户为管理员用户.
     *         如果返回false, 说明两用户不是同一用户或不是管理员用户
     */
    public static boolean confirmCurrentUser(User confirm, User template) {
        if (template.getId() != confirm.getId() && !confirm.isSuper()){
             return false;
        }
            return true;
    }

}
