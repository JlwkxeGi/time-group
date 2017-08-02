package mycourse.onkshare.api.service;


import mycourse.onkshare.model.File;
import mycourse.onkshare.model.FileComment;
import mycourse.onkshare.model.FileDesc;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.ObjectResult;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface FileCommentService {

  PagingResult getFileComments(Long fileId, Long parentId, Integer startPage, Integer pageSize);

  ObjectResult appendFileComment(Long fileId,Long id, String content, Long floor, User current);

}
