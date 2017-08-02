package mycourse.onkshare.api.web;

import mycourse.onkshare.exception.CustomRuntimeException;
import mycourse.onkshare.api.service.NotificationService;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by doll on 4/1.
 */
@Controller
public class NotificationController extends BaseController {

    @Resource(name="notificationService")
    NotificationService notificationService;

    @RequestMapping("/notification/show")
    @ResponseBody
    public Result showNotification(@RequestParam(value = "s", defaultValue = "0") int startPage,
                                   @RequestParam(value = "l", defaultValue = "10") int pageSize) {
        PagingResult result = notificationService.showNotification(startPage, pageSize);
        return result;
    }

    @RequestMapping("/notification/append")
    @ResponseBody
    public Result appendNotification(@RequestParam(value = "content", defaultValue = "---") String content) {

        Result result = notificationService.appendNotification(content);
        return result;
    }

    @RequestMapping("/notification/remove")
    @ResponseBody
    public Result removeNotification(int id) {
        try {
            Result result = notificationService.removeNotification(id, getSessionUser());
            return  result;
        }catch (CustomRuntimeException ce) {
            ce.printStackTrace();
            Result error = Result.error(ce.getMessage());
            return error;
        }catch (Exception e) {
            e.printStackTrace();
            return Result.Error;
        }
    }
}
