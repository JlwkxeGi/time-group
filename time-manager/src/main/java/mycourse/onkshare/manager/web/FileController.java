package mycourse.onkshare.manager.web;

import mycourse.onkshare.common.util.JSONUtil;
import mycourse.onkshare.exception.CustomRuntimeException;
import mycourse.onkshare.manager.service.FileRedisService;
import mycourse.onkshare.model.File;
import mycourse.onkshare.model.FileDesc;
import mycourse.onkshare.model.result.ObjectResult;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.model.User;
import mycourse.onkshare.manager.service.FileService;
import mycourse.onkshare.model.result.SearchResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

@Controller
public class FileController extends BaseController{

	@Resource(name="fileService")
	private FileService fileService;


/*	@RequestMapping("/file/list")
	@ResponseBody
	public Result fileList(@RequestParam int rows, @RequestParam int page) {
			PagingResult result = new PagingResult();
			result.setPageSize(rows);
			result.setPageNumber(page);
			PagingResult fileList = fileService.getFilePagingList(result);
		return fileList;
	}*/
	@RequestMapping("/file/list")
	@ResponseBody
	public Result fileList(@RequestParam(value = "id" , defaultValue = "0") Long parentId,
						 @RequestParam(value = "s", defaultValue="1") Integer startPage,
						   @RequestParam(value = "e", defaultValue = "10") Integer pageSize,
						   @RequestParam(value = "pag", defaultValue = "0") Integer pagination) {

		if (parentId == 0) {
			PagingResult filePagingList = fileService.getUsersTopFolderPagingList(startPage, pageSize, getSessionUser());
			return filePagingList;
		}
		List<File> files = fileService.getFileList(parentId, getSessionUser());
		/*List list = new ArrayList();
		for ( File item: files) {
			Map<String, Object> tree = new HashMap<String, Object>();
			tree.put("id",item.getId());
			tree.put("text",item.getName());
			tree.put("url", item.getUrl());
			tree.put("state", item.getChildren() != -1 ? "closed":"open");
			tree.put("folder", item.getChildren() != -1 ? true : false);
			tree.put("attraction",item.getAttraction());
			tree.put("created", item.getCreated());
			tree.put("updated", item.getUpdated());
			tree.put("level",item.getLevel());
			tree.put("status", item.getStatus());
			switch (item.getStatus()) {
				case 0: tree.put("checked", false); break;
				case 1: tree.put("checked", true); break;
				case 2: tree.put("indeterminate", true); tree.put("checked", false); break;
			}
			tree.put("worth", item.getWorth());
			list.add(tree);
		}*/
		if (files == null || files.isEmpty()) {
			return null;
		}
		PagingResult result = new PagingResult();
		result.setRows(files);
		result.setError(0);
		result.setMessage("success");
		return result;
	}

	@RequestMapping("/file/list/byUser")
	@ResponseBody
	public List fileOneList(@RequestParam(value = "id" ) Long fileId) {
		User userByFileId = fileService.findUserByFileId(fileId);
		List<File> files = fileService.getFileListByUser(userByFileId, getSessionUser());
		List list = new ArrayList();
		for ( File item: files) {
			Map<String, Object> tree = new HashMap<String, Object>();
			tree.put("id",item.getId());
			tree.put("text",item.getName());
			tree.put("url", item.getUrl());
			tree.put("state", item.getChildren() != -1 ? "closed":"open");
			tree.put("folder", item.getChildren() != -1 ? true : false);
			tree.put("attraction",item.getAttraction());
			tree.put("created", item.getCreated());
			tree.put("updated", item.getUpdated());
			tree.put("level",item.getLevel());
			tree.put("status", item.getStatus());
			switch (item.getStatus()) {
				case 0: tree.put("checked", false); break;
				case 1: tree.put("checked", true); break;
				case 2: tree.put("indeterminate", true); tree.put("checked", false); break;
			}
			tree.put("worth", item.getWorth());
			list.add(tree);
		}
		return list;
	}

	@RequestMapping("file/share/list")
	@ResponseBody
	public Result shareList(@RequestParam(value = "id") Long id) {
		User userByFileId = fileService.findUserByFileId(id);
		List<File> files = fileService.getShareList(userByFileId, getSessionUser());
		/*List list = new ArrayList();
		for ( File item: files) {
			Map<String, Object> tree = new HashMap<String, Object>();
			tree.put("id",item.getId());
			tree.put("text",item.getName());
			tree.put("url", item.getUrl());
			tree.put("state", item.getChildren() != -1 ? "closed":"open");
			tree.put("folder", item.getChildren() != -1 ? true : false);
			tree.put("attraction",item.getAttraction());
			tree.put("level",item.getLevel());
			tree.put("created", item.getCreated());
			tree.put("updated", item.getUpdated());
			tree.put("status", item.getStatus());
			switch (item.getStatus()) {
				case 0: tree.put("checked", false); break;
				case 1: tree.put("checked", true); break;
				case 2: tree.put("indeterminate", true); tree.put("checked", false); break;
			}
			tree.put("worth", item.getWorth());
			list.add(tree);
		}*/
		PagingResult result = new PagingResult();
		result.setRows(files);
		return result;
	}

	@RequestMapping("/file/share/update")
	@ResponseBody
	public Result shareUpdate(String checkedIds, String uncheckedIds, String indeterminateIds) {
		if (checkedIds == null) return Result.Error;
		if (uncheckedIds == null) return Result.Error;
		if (indeterminateIds == null) return Result.Error;

		/*
			格式化,checkedIds转为Long[]
		 */

		String[] split = checkedIds.split(",");
		Long[] reIds = new Long[split.length];
		for (int i = 0, j = 0; i < split.length; i++ ) {
			if (!"undefined".equals(split[i])) {
				reIds[j++] = Long.valueOf(split[i]);
			}
		}
		/*
			格式化, uncheckedIds转为Long[]
		 */
		String[] splitUn = uncheckedIds.split(",");
		Long[] reUnIds = new Long[splitUn.length];
		for (int i = 0, j = 0; i < splitUn.length; i++ ) {
			if (!"undefined".equals(splitUn[i])) {
				reUnIds[j++] = Long.valueOf(splitUn[i]);
			}
		}

		/*
			格式化, indeterminate转为Long[]
		 */
		String[] splitInd = indeterminateIds.split(",");
		Long[] reIndIds = new Long[splitInd.length];
		for (int i = 0, j = 0; i < splitInd.length; i++ ) {
			if (!"undefined".equals(splitInd[i])) {
				reIndIds[j++] = Long.valueOf(splitInd[i]);
			}
		}

		try {
			Result result = fileService.updateSharedFile(reIds, reUnIds, reIndIds);
			return result;
		}catch (Exception e) {
			return  Result.error(e.getMessage());
		}
	}

	@RequestMapping(value = "/file/addFile", method = RequestMethod.POST)
	@ResponseBody
	public Result addFile(MultipartFile[] uploadFile, File filepath) {
		if (uploadFile == null || uploadFile.length == 0) {
			return Result.Error;
		}
		User user = getSessionUser();
		if (user == null || user.getId() == 0) {
			return Result.Error;
		}
		Result result = fileService.addFile(uploadFile,user ,filepath);
		return result;
	}

	@RequestMapping("/file/addFolder")
	@ResponseBody
	public Map addFolder(Long id, String text, String attraction) {
		Map<String, Object> map = new HashMap();
		User user = getSessionUser();
		if (user == null || user.getId() == 0) {
			map.put("message", "error");
			map.put("error", 1);
			return map;
		}

		if (text == null || "".equals(text.trim())) {
			map.put("message", "error");
			map.put("error", 1);
			return map;
		}

		if (attraction == null || "".equals(attraction.trim())) {
			map.put("message", "error");
			map.put("error", 1);
			return map;
		}

		File item = null;
		try {
			item  = fileService.addFolder(id, text.trim(),attraction, user);
		}catch (Exception e) {
			map.put("error", 1);
			map.put("message", e);
			return map;
		}finally {
			if (item == null) {
				map.put("error", 1);
				map.put("message", "文件夹添加失败!!");
				return map;
			}
		}


		map.put("id",item.getId());
		map.put("text",item.getName());
		map.put("state", item.getChildren() != -1 ? "closed":"open");
		map.put("folder", item.getChildren() != -1 ? true : false);
		map.put("url", item.getUrl());
		map.put("attraction", item.getAttraction());
		map.put("level", item.getLevel());
		map.put("status", item.getStatus());
		map.put("created", item.getCreated());
		map.put("updated", item.getUpdated());
		map.put("size", item.getSize());
		map.put("type", item.getType());
		map.put("message", "success");
		map.put("error", 0);
		return map;
	}


	@RequestMapping("file/uploadFolder")
	public Result  uploadFolder(HttpServletRequest request){
	    User user = (User)request.getSession().getAttribute(WebConstant.LOGINUSER);
	    if (user == null) {
            Result erorr = Result.error("请先登录!!");
            return erorr;
        }
		long  startTime=System.currentTimeMillis();
		//将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//检查form中是否有enctype="multipart/form-data"
		if(multipartResolver.isMultipart(request))
		{
			//将request变成多部分request
			MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
			fileService.uploadFolder(multiRequest, user);

		}
		long  endTime=System.currentTimeMillis();
		System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
		return Result.SUCCESS;
	}

	@RequestMapping("/file/delete")
	@ResponseBody
	public Result deleteFile(@RequestParam(value = "ids", required = true) String ids) {
		Map errors = new HashMap();
		if (ids == null || ids.length() == 0 ) {
			errors.put("ids", "ids为空");
		}
		String[] split = ids.split(",");
		Long[] idArr = new Long[split.length];
		for (int i =0; i< split.length; i++){
			idArr[i] = Long.valueOf(split[i]);
		}
		Result result = fileService.deleteFile(idArr, getSessionUser());
		return result;
	}

	@RequestMapping("/file/update")
	@ResponseBody
	public Result updateFile(Long id, String text,  Boolean folder) {
		Map errors = new HashMap();
		if (id == null || id == 0) {
			errors.put("id", "id为空");
		}
		if (text == null || "".equals(text)) {
			errors.put("name", "name为空");
		}
		if (folder == null) {
			errors.put("folder", "folder错了!!");
		}
		Result result = fileService.updateFile(id, text, folder, getSessionUser());
		return result;
	}

	@RequestMapping("/file/move")
	@ResponseBody
	public Result updateFile(Long parentId , Long targetFileId) {

		if (parentId == null || parentId <= 0) {
			return Result.Error;
		}
		if (targetFileId == null || targetFileId <= 0) {
			return Result.Error;
		}

		try {
				Result result = fileService.moveFile(parentId, targetFileId, getSessionUser());

				return result;
		}catch (Exception e) {
			e.printStackTrace();
			return  Result.error(e.getMessage());
		}
	}


	@RequestMapping("file/details")
	@ResponseBody
	public Result getDetails(Long id) {
		if (id == null || id == 0) {
			return  null;
		}
		FileDesc fileDesc = fileService.getFileDesc(id);
		ObjectResult result = new ObjectResult();
		result.setObject(fileDesc);
		result.setMessage("success");
		return result;
	}


	@RequestMapping("file/edit/detailsAndBrief")
	@ResponseBody
	public Result editDetailsAndBrief(Long detailsId , String desc , String attraction) {
		if (detailsId == null || detailsId == 0) {
			return  Result.Error;
		}
		if (desc == null || "".equals(desc)) {
			return  Result.Error;
		}
		try {
			Result result = fileService.editDetailsAndBrief(detailsId, desc,attraction, getSessionUser());
			return  result;
		}catch (Exception e) {
			Result result = Result.error(e.getMessage());
			return result;
		}
	}

	@RequestMapping("file/download")
	@ResponseBody
	public String downloadFile(@RequestParam(required = true)  Long id) {

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		String path = fileService.downloadFile(id, getSessionUser());
		try {
			java.io.File file = new java.io.File(path);
			if (!file.exists()) throw new CustomRuntimeException("路径不存在!!");
			HttpHeaders headers = new HttpHeaders();
			response.addHeader("Content-Disposition","attachment;filename=" + file.getName());
			//response.setContentLengthLong(file.length());
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			int available = bis.available();
			bos = new BufferedOutputStream(response.getOutputStream());
			int position = 0;
			byte[] b = new byte[4096 * 2];
			do {
				position = bis.read(b);
				bos.write(b);
			}while (position != -1);
			bos.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (bis != null) {
				try {bis.close();}catch (Exception e){ e.printStackTrace();}
			}
		}
		return null;
	}

	@RequestMapping("/search")
	@ResponseBody
	public Result search(@RequestParam(value = "q", required = true) String keyword,
						 @RequestParam(value = "s", defaultValue="1") Integer startPage,
						 @RequestParam(value = "e", defaultValue = "10") Integer pageSize,
						 @RequestParam(value = "shared", defaultValue = "1") Integer shared,
						 @RequestParam(value = "id", defaultValue = "0") Long parentId) {

			if (parentId != 0) {
				List<File> files = fileService.getShareFileList(parentId);
				List list = new ArrayList();
				for ( File item: files) {
					Map<String, Object> tree = new HashMap<String, Object>();
					tree.put("id",item.getId());
					tree.put("text",item.getName());
					tree.put("url", item.getUrl());
					tree.put("state", item.getChildren() != -1 ? "closed":"open");
					tree.put("folder", item.getChildren() != -1 ? true : false);
					tree.put("attraction",item.getAttraction());
					tree.put("created", item.getCreated());
					tree.put("updated", item.getUpdated());
					tree.put("level",item.getLevel());
					tree.put("status", item.getStatus());
					switch (item.getStatus()) {
						case 0: tree.put("checked", false); break;
						case 1: tree.put("checked", true); break;
						case 2: tree.put("indeterminate", true); tree.put("checked", false); break;
					}
					tree.put("worth", item.getWorth());
					list.add(tree);
				}
				SearchResult searchResult = new SearchResult();
				searchResult.setPageNumber(startPage);
				searchResult.setPageSize(pageSize);
				searchResult.setRows(list);
				searchResult.setTotal(list.size());
				return searchResult;
			}

		try {
			String key = URLEncoder.encode(keyword , "utf-8");
			String path = WebConstant.SEARCH_FILE_PATH + "/search?q=" +key + "&s=" + startPage + "&e=" + pageSize + "&shared=" + shared;
			URL url = new URL(path);
			HttpClient httpClient = new HttpClient();
			HttpMethod httpMethod = new GetMethod(path);
			int i = httpClient.executeMethod(httpMethod);
			String bodyAsString = httpMethod.getResponseBodyAsString();
			httpMethod.releaseConnection();
			JSONObject jsonObject = JSONUtil.JSONToResult(bodyAsString);
			if (jsonObject == null) {
				return Result.SUCCESS;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			Iterator iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject next = (JSONObject) iterator.next();
				int file_children = (int)next.get("children");
				next.put("state", file_children !=-1 ? "closed" : "open");
				Object type = next.get("type");
				if (type == null || type instanceof  JSONNull) {
					next.remove("type");
					next.put("type", "");
				}
				Object size = next.get("size");
				if (size == null || size instanceof JSONNull) {
					next.remove("size");
					next.put("size", 0L);
				}
				System.out.println(next);
			}
			SearchResult searchResult = new SearchResult();
			searchResult.setPageNumber(startPage);
			searchResult.setPageSize(pageSize);

			searchResult.setRows(jsonArray);
			long total = jsonObject.getLong("total");
			searchResult.setTotal(total);
			return searchResult;
		}catch (Exception e) {
				e.printStackTrace();
				return Result.Error;
		}

	}

	@RequestMapping("file/copy")
	@ResponseBody
	public Result copyFiles(@RequestParam(value = "cIds", required = true) Long[] ids,
							@RequestParam(value = "tfId", required = true) Long folderId) {
			Result result = fileService.copyFiles(ids, folderId, getSessionUser());
			return result;
	}


}
