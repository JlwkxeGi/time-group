package mycourse.onkshare.api.service;

import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.Result;

/**
 * Created by doll on 4/11.
 */
public interface LogService {

    public Result findUserLoginLog(User current);
}
