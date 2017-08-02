package mycourse.onkshare.client.web;

import mycourse.onkshare.api.service.FileCommentService;
import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.stereotype.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by doll on 3/29.
 */
@Controller
public class FileCommentController extends BaseController {

    @Resource(name = "fileCommentService")
    FileCommentService fileCommentService;

    @RequestMapping("/fc/comment")
    @ResponseBody
    @Permission
    public Result showMessage(@RequestParam(value = "fid" , defaultValue = "0", required = true) Long fileId,
                              @RequestParam(value = "id" , defaultValue = "0") Long parentId,
                              @RequestParam(value = "s", defaultValue="1") Integer startPage,
                              @RequestParam(value = "e", defaultValue = "10") Integer pageSize) {
        try {
            Result message = fileCommentService.getFileComments(fileId, parentId, startPage, pageSize);
            return message;
        }catch (Exception e) {
            e.printStackTrace();
            return Result.Error;
        }
    }

    @RequestMapping("/fc/append")
    @ResponseBody
    @Permission(Permission.USER)
    public Result appendMessage(@RequestParam(value = "id" , required = true) Long parentId,
                                @RequestParam(value = "fid" , defaultValue = "0") Long fileId,
                                String content,
                                @RequestParam(value = "floor" , defaultValue = "1") Long floor) {
        if (floor < 1) {
            floor = 1L;
        }
        Result result = fileCommentService.appendFileComment(fileId, parentId, content,floor, getSessionUser());
        return  result;
    }
}
