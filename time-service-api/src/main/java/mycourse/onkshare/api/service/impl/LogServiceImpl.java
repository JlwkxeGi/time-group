package mycourse.onkshare.api.service.impl;

import mycourse.onkshare.api.service.LogService;
import mycourse.onkshare.mapper.LoginLogMapper;
import mycourse.onkshare.model.LoginLog;
import mycourse.onkshare.model.LoginLogExample;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.ObjectResult;
import mycourse.onkshare.model.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by doll on 4/11.
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Resource
    LoginLogMapper loginLogMapper;

    @Override
    public Result findUserLoginLog(User current) {

        List<LoginLog> loginLogs = loginLogMapper.selectFrontFew(current.getId());
        ObjectResult result = new ObjectResult();
        result.setObject(loginLogs);
        return result;
    }
}
