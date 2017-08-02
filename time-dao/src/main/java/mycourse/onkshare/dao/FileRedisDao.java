package mycourse.onkshare.dao;

/**
 * Created by doll on 3/1.
 */
public interface FileRedisDao {

    String getFilePath(String identify);

    boolean setFilePath(String identify, String path);

    boolean deleteFile(String identify);
}
