package mycourse.onkshare.dao.impl;

import mycourse.onkshare.dao.FileRedisDao;
import mycourse.onkshare.encoder.Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by doll on 3/1.
 */
@Repository("fileRedisDao")
public class FileRedisDaoImpl implements FileRedisDao {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private Encoder encoder;

    @Value("${fileIdentifyEnter}")
    private String fileIdentifyEnter;
    @Value("${fileIdentifyHoldEnter}")
    private String fileIdentifyHoldEnter;

    @Override
    public String getFilePath(final String identify) {
        final String root = fileIdentifyEnter + "." + encoder.getMethodName();
        Boolean aBoolean = redisTemplate.hasKey(fileIdentifyEnter + "." + encoder.getMethodName());
        if (!aBoolean) return  null;
        Object execute = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] bytes = connection.hGet(root.getBytes(), identify.getBytes());
                if (bytes == null) return null;
                String path = new String(bytes);
                return path;
            }
        });
        if (execute instanceof String ) {
            String s = (String)execute;
            return s;
        }
        return  null;
    }

    @Override
    public boolean setFilePath(final String identify, final String path) {
        final String root = fileIdentifyEnter + "." + encoder.getMethodName();
        final String holdId = fileIdentifyHoldEnter + "." + encoder.getMethodName();
        Object execute = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.hSet(root.getBytes(), identify.getBytes(), path.getBytes());
                connection.hSet(holdId.getBytes(), identify.getBytes(), "1".getBytes());
                return true;
            }
        });
        if (execute instanceof Boolean) {
            Boolean b = (Boolean)execute;
            return b;
        }
        return false;
    }


    @Override
    public boolean deleteFile(final String identify) {
        final String holdId = fileIdentifyHoldEnter + "." + encoder.getMethodName();
        final String root = fileIdentifyEnter + "." + encoder.getMethodName();
        Object execute = redisTemplate.execute(new RedisCallback() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] bytes = connection.hGet(holdId.getBytes(), identify.getBytes());
                if (bytes == null) return false;
                String s = new String(bytes);
                try {
                    Long hold = Long.valueOf(s);
                    hold -= 1;
                    String s1 = String.valueOf(hold);
                    if (hold > 0) {
                        connection.hSet(holdId.getBytes(), identify.getBytes(), s1.getBytes());
                    } else {
                        connection.hDel(holdId.getBytes(), identify.getBytes());
                        connection.hDel(root.getBytes(), identify.getBytes());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        });

        boolean b = (Boolean)execute;
        return b;
    }
}
