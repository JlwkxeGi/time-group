package mycourse.onkshare.client.web;

import mycourse.onkshare.api.service.LogService;
import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.stereotype.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by doll on 4/11.
 */
@Controller
public class LogController extends BaseController {

    @Resource(name = "logService")
    LogService logService;

    @RequestMapping("/log/loginl")
    @ResponseBody
    @Permission(Permission.USER)
    public Result loginLog() {
        Result result = logService.findUserLoginLog(getSessionUser());
        return  result;
    }
}
