package mycourse.onkshare.api.service.impl;

import mycourse.onkshare.api.service.NotificationService;
import mycourse.onkshare.mapper.NotificationMapper;
import mycourse.onkshare.model.Notification;
import mycourse.onkshare.model.NotificationExample;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.ObjectResult;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by doll on 4/1.
 */
@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    @Resource
    NotificationMapper notificationMapper;

    @Override
    public PagingResult showNotification(int startPage, int length) {
        PagingResult result = new PagingResult();
        result.setPageNumber(startPage);
        result.setPageSize(length);
        Map conditions = new HashMap();
        conditions.put("start", (result.getPageNumber()-1)* length);
        conditions.put("length", length);
        List<Notification> notifications =  notificationMapper.selectNotificationForPagination(conditions);
        result.setRows(notifications);
        long l = notificationMapper.countByExample(null);
        result.setTotal(l);
        return result;
    }

    @Override
    public ObjectResult appendNotification(String content) {
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setCreated(new Date());
        notification.setUpdated(new Date());
        Long insert = notificationMapper.insert(notification);
        ObjectResult result = new ObjectResult();
        result.setObject(notification);
        return result;
    }

    @Override
    public Result removeNotification(Integer id, User current) {
        if (!current.isSuper()) {
            throw new ClassCastException("没有权限!!");
        }
        int i = notificationMapper.deleteByPrimaryKey(id);
        if (i > 0) {
            return Result.SUCCESS;
        }
        return Result.Error;
    }
}
