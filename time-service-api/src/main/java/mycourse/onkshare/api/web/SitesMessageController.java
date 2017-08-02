package mycourse.onkshare.api.web;

import mycourse.onkshare.api.service.SitesMessageService;
import mycourse.onkshare.model.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class SitesMessageController extends BaseController{

	@Resource(name="sitesMessageService")
	SitesMessageService sitesMessageService;

	@RequestMapping("/sites/message")
	@ResponseBody
	public Result showMessage(@RequestParam(value = "id" , defaultValue = "0") Long parentId,
							  @RequestParam(value = "s", defaultValue="1") Integer startPage,
							  @RequestParam(value = "e", defaultValue = "10") Integer pageSize) {
		try {
			Result message = sitesMessageService.getMessage(parentId, startPage, pageSize);
			return message;
		}catch (Exception e) {
			e.printStackTrace();
			return Result.Error;
		}
	}

	@RequestMapping("/sites/appendMessage")
	@ResponseBody
	public Result appendMessage(@RequestParam(value = "id" , required = true) Long parentId,
								String content,
								@RequestParam(value = "floor" , defaultValue = "1") Long floor) {
		if (floor < 1) {
			floor = 1L;
		}
		Result result = sitesMessageService.appendMessage(parentId, content,floor, getSessionUser());
		return  result;
	}
}
