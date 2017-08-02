package mycourse.onkshare.manager.service;

import mycourse.onkshare.model.File;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.Result;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Created by doll on 2/27.
 */
public interface FileRedisService {

    Result uploadFolder(MultipartHttpServletRequest multiRequest, User currentUser);

    Result removeFile(Long id, User currentUser);
}
