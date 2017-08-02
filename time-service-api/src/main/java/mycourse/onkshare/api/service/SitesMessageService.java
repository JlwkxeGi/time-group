package mycourse.onkshare.api.service;


import mycourse.onkshare.model.File;
import mycourse.onkshare.model.FileDesc;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface SitesMessageService {

   public Result getMessage(Long parentId, Integer startPage, Integer pageSize);

   public Result appendMessage(Long id, String content, Long floor, User current);

}
