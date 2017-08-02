package mycourse.onkshare.manager.service;

import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.ObjectResult;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;

/**
 * Created by doll on 4/1.
 */
public interface NotificationService {

    public PagingResult showNotification(int startPage, int length);

    public ObjectResult appendNotification(String content);

    Result removeNotification(Integer id, User current);
}
