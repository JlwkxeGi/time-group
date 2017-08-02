package mycourse.onkshare.manager.web;

import mycourse.onkshare.common.util.JSONUtil;
import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.exception.CustomRuntimeException;
import mycourse.onkshare.manager.service.FileService;
import mycourse.onkshare.manager.service.SitesMessageService;
import mycourse.onkshare.model.File;
import mycourse.onkshare.model.FileDesc;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.ObjectResult;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.model.result.SearchResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

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
