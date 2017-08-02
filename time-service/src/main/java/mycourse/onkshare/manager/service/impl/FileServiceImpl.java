package mycourse.onkshare.manager.service.impl;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import javax.annotation.Resource;

import mycourse.onkshare.common.util.ConfirmUtil;
import mycourse.onkshare.constant.e.StorageOption;
import mycourse.onkshare.encoder.*;
import mycourse.onkshare.mapper.FileDescMapper;
import mycourse.onkshare.mapper.FileMapMapper;
import mycourse.onkshare.mapper.FileMapper;
import mycourse.onkshare.mapper.UserMapper;
import mycourse.onkshare.model.*;
import mycourse.onkshare.common.util.IOUtil;
import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.exception.CustomRuntimeException;
import mycourse.onkshare.model.File;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.manager.service.FileService;
import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Service("fileService")
public class FileServiceImpl implements FileService {

	@Resource
	private FileMapper fileMapper;
	@Resource
	private FileDescMapper fileDescMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private FileMapMapper fileMapMapper;

	@Resource
	private Encoder encoder;
/*
	@Override
	public Number getFileCount() {
		Number count = FileMapper.selectFileCount();
		return count;
	}*/

	@Override
	public List<File> getFileList(Long parentId, User user){
		FileExample example = new FileExample();
		FileExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		if (!user.isSuper()) {
			criteria.andUserIdEqualTo(user.getId());
		}
		example.setOrderByClause("children desc, status desc, name desc");
		List<File> file = fileMapper.selectByExample(example);
		return file;
	}

	@Override
	public List<File> getShareFileList(Long parentId){
		FileExample example = new FileExample();
		FileExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		byte b = 1;
		criteria.andStatusEqualTo(b);

		example.setOrderByClause("children desc, status desc, name desc");
		List<File> file = fileMapper.selectByExample(example);
		return file;
	}


	@Override
	public List getFileListByUser(User target, User current) {
		FileExample example = new FileExample();
		FileExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(target.getId());
		List<File> file = fileMapper.selectByExample(example);
		return file;
	}

	@Override
	public PagingResult getUsersTopFolderPagingList(Integer startPage, Integer pageSize, User user) {
		PagingResult result = new PagingResult();
		result.setPageSize(pageSize);
		result.setPageNumber(startPage);
		Map<String, Object> map = new HashMap();
		map.put("startPage",(result.getPageNumber()-1)*result.getPageSize());
		map.put("pageSize",result.getPageSize());
		List<File> files =  fileMapper.selectUsersTopFolderByParentIdForPagination(map);
		result.setRows(files);
		FileExample example = new FileExample();
		example.createCriteria().andParentIdEqualTo(0L);
		result.setTotal(fileMapper.countByExample(example));
		return result;
	}

	public File getFolder(String folderName ,Long userId, Long parentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", parentId);
		map.put("userId", userId);
		map.put("folderName", folderName);
		File file = fileMapper.selectFileByUserIdAndParentIdAndName(map);
		return file;
	}

	public File getFolder(String url ,Long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("url", url);
		File file = fileMapper.selectFileByUserIdAndUrl(map);
		return file;
	}

	public File getFolder(Long id) {
		File file = fileMapper.selectByPrimaryKey(id);
		return file;
	}

	@Override
	public File getFile(Long id) {
		File file = fileMapper.selectByPrimaryKey(id);
		return file;
	}

	@Override
	public Result addFile(MultipartFile[] uploadFiles, User user, File filepath){
		int result = 0;
		try{
			if (filepath.getChildren() == -1) {
				Result result1 = Result.error("请选择文件夹!!");
				return result1;
			}
			for (MultipartFile uploadFile: uploadFiles) {
				String originalFilename = uploadFile.getOriginalFilename();
				String path = WebConstant.TEMPDIRTORY + user.getUsername() + filepath.getUrl() + originalFilename;
				java.io.File file = new java.io.File(path);
				uploadFile.transferTo(file);
			}
			result = 1;
		} catch (Exception e){
			e.printStackTrace();
		}
		finally {
			if (result == 0) {
				return Result.error("insert File fail!!");
			}
		}
		return Result.SUCCESS;
	}

	@Override
	public File addFolder(Long id, String text,String attraction, User currentUser) {
		try {
			File parentFolder = fileMapper.selectByPrimaryKey(id);
			if (parentFolder == null) {
				throw new CustomRuntimeException("文件夹不存在!!");
			}
			if (parentFolder.getChildren() == -1) {
				parentFolder = getFolder(parentFolder.getParentId());
			}
			/*
				判断用户权限
			 */
			User user = userMapper.selectByPrimaryKey(parentFolder.getUserId());
			boolean privilege = ConfirmUtil.confirmCurrentUser(currentUser, user);
			if (!privilege) {
				throw  new CustomRuntimeException("你没有权限访问!!");
			}

			String uri = parentFolder.getUrl() + text + "/";
			String path = WebConstant.ROOTFOLDER + user.getUsername() + "/" + parentFolder.getUrl() + text + "/";

			File folder = new File();
			folder.setCreated(new Date());
			folder.setUpdated(new Date());
			folder.setChildren(0);
			folder.setName(text);
			folder.setLevel(parentFolder.getLevel() + 1);
			folder.setParentId(parentFolder.getId());
			folder.setUrl(uri);
			folder.setStorageId("folder");
			folder.setStorageMethod(encoder.getMethodName());
			folder.setUserId(user.getId());
			switch (parentFolder.getStatus()) {
				case 0:
				case 1:folder.setStatus(parentFolder.getStatus()); break;
				default:break;
			}
			if (attraction != null) folder.setAttraction(attraction);

			Long flag = fileMapper.insert(folder);

			/*java.io.File file = new java.io.File(path);
			if (!file.exists()) {
				boolean mkFlag = file.mkdirs();
				if (!mkFlag) throw new CustomRuntimeException("文件夹添加失败!!");
			}*/
			Map map = new HashMap();
			map.put("userId", user.getId());
			map.put("url", uri);
			File target = fileMapper.selectFileByUserIdAndUrl(map);
			return target;
		}catch (Exception e) {
				e.printStackTrace();
				throw new CustomRuntimeException("文件夹添加失败!!");
		}
	}

	@Override
	public Result uploadFolder(MultipartHttpServletRequest multiRequest, User currentUser) {
		//指定的上传路径
		String uploadPath = multiRequest.getParameter("uploadPathId");

		if (uploadPath == null || uploadPath.isEmpty()) return Result.Error;

		Long id= Long.valueOf(uploadPath);
		File realUploadPath = getFolder(id);
		if (realUploadPath == null ||realUploadPath.getChildren() == -1) {
			return Result.Error;
		}

		/*
			判断权限
		 */
		User user = userMapper.selectByPrimaryKey(realUploadPath.getUserId());
		boolean privilege = ConfirmUtil.confirmCurrentUser(currentUser, user);
		if (!privilege) throw  new CustomRuntimeException("你没有权限访问!!");

		//获取multiRequest 中所有的文件名
		Iterator iter=multiRequest.getFileNames();

		while(iter.hasNext()){
			//一次遍历所有文件
			List<MultipartFile> files=multiRequest.getFiles(iter.next().toString());
			if(files!=null){
				for (MultipartFile multipartFile: files) {
					CommonsMultipartFile tempFile = (CommonsMultipartFile)multipartFile;
					FileItem fileItem = tempFile.getFileItem();
					String name = fileItem.getName();
					int end = name.length() - multipartFile.getOriginalFilename().length();
					String path = name.substring(0, end);
					String[] paths = path.split("/");


					File parentFolder = realUploadPath;
					int length = paths.length;
					if (path.trim().length() > 0) {
						for (int i = 0; i < paths.length; i++) {
                            /*
                                判断路径是否已存在
                             */
							File folder = getFolder(parentFolder.getUrl() + paths[i] + "/", user.getId());
							if (folder != null) {
								parentFolder = folder;
							}else {
								File folder2 = new File();
								folder2.setParentId(parentFolder.getId());
								folder2.setUrl(parentFolder.getUrl() + paths[i] + "/");
								folder2.setName(paths[i]);
								folder2.setUserId(user.getId());
								folder2.setChildren(0);
								folder2.setCreated(new Date());
								folder2.setUpdated(new Date());
								folder2.setStorageId("folder");
								folder2.setStorageMethod("folder");
								folder2.setLevel(realUploadPath.getLevel() + i + 1);
								switch (parentFolder.getStatus()) {
									case 0:
									case 1:folder2.setStatus(parentFolder.getStatus()); break;
									default:break;
								}
								fileMapper.insert(folder2);

								File folder3 = getFolder(folder2.getName(), user.getId(), parentFolder.getId() );
								parentFolder = folder3;
							}
						}
					}

					//上传
					try {

                        /*
                            先查找文件是否已存在
                         */
						String encode = "";
						InputStream is = multipartFile.getInputStream();
						if (is instanceof  ByteArrayInputStream){
							ByteBuffer byteBuffer = ByteBuffer.wrap(multipartFile.getBytes());
							encode = encoder.getFileIdentify(byteBuffer);
						}else if (is instanceof FileInputStream) {
							 encode = encoder.getFileIdentify((FileInputStream) is);
						}
							String identify = encode + "_" + multipartFile.getSize();
							FileMapKey key = new FileMapKey();
							key.setStorageId(identify);
							key.setStorageMethod(encoder.getMethodName());
							FileMap fileMap = fileMapMapper.selectByPrimaryKey(key);
							if (fileMap == null) {
                            /*
                            * */
								String byTimeUuid = IOUtil.getFileNameByTimeUuid(multipartFile.getOriginalFilename());
								String rootPathByTime = IOUtil.getRootPathByTime(WebConstant.ROOTFOLDER, StorageOption.HOUR);
								fileMap = new FileMap();
								fileMap.setStorageMethod(encoder.getMethodName());
								fileMap.setStorageId(identify);
								fileMap.setHold(0L);
								fileMap.setPath(rootPathByTime + "/" + byTimeUuid);
								int flag = fileMapMapper.insert(fileMap);
								if (flag == 0) throw new CustomRuntimeException("文件上传出错!!");
								java.io.File fPath = new java.io.File(rootPathByTime);
								if (!fPath.exists()) {
									fPath.mkdirs();
								}
								java.io.File finalPath = new java.io.File(rootPathByTime + "/" + byTimeUuid);
								if (!finalPath.exists()) {
									finalPath.createNewFile();
								}
								multipartFile.transferTo(finalPath);
							}

							File file = new File();
							file.setParentId(parentFolder.getId());
							file.setUrl(parentFolder.getUrl() + multipartFile.getOriginalFilename());
							file.setName(multipartFile.getOriginalFilename());
							file.setUserId(user.getId());
							file.setChildren(-1);
							file.setCreated(new Date());
							file.setUpdated(new Date());
							file.setSize(multipartFile.getSize());
							file.setStorageId(identify);
							file.setStorageMethod(encoder.getMethodName());
							//file.setType(multipartFile.getContentType());
							file.setLevel(parentFolder.getLevel() + 1);
							switch (parentFolder.getStatus()) {
							case 0:
								case 1:file.setStatus(parentFolder.getStatus()); break;
							default:break;
						}

							/*
								判断url是否已存在
							 */
							FileExample example = new FileExample();
							FileExample.Criteria criteria = example.createCriteria();
							String original = multipartFile.getOriginalFilename();
							String originalFilename = IOUtil.originalName(original);
							int last = originalFilename.lastIndexOf(".");
							String na = "";
							String la = "";
							if (last > 0) {
								na = originalFilename.substring(0, last);
								la = originalFilename.substring(last, originalFilename.length());
							}
							criteria.andUrlLike(parentFolder.getUrl() + na + "%" + la);
							criteria.andUserIdEqualTo(user.getId());
							example.setOrderByClause("url");
							List<File> files1 = fileMapper.selectByExample(example);
							if (files1 != null && files1.size() != 0) {
								String url = files1.get(0).getUrl();
								String n = files1.get(0).getName();
								int count = 0;
								for ( File item: files1) {
									int number = IOUtil.getFileCopyNameNumber(item.getUrl());
									if (number > count) {
										count = number;
										url = item.getUrl();
										n = item.getName();
									}
								}

								String rename = IOUtil.rename(url, count + 1);
								file.setUrl(rename);
								String rename1 = IOUtil.rename(n, count + 1);
								file.setName(rename1);
							}
							fileMap.setHold(fileMap.getHold() + 1);
							fileMapMapper.updateByPrimaryKey(fileMap);
							fileMapper.insert(file);

					}catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}

		}
		return Result.SUCCESS;
		/*//指定的上传路径
		String uploadPath = multiRequest.getParameter("uploadPathId");

		if (uploadPath == null || uploadPath.isEmpty()) return Result.Error;

		Long id= Long.valueOf(uploadPath);
		File realUploadPath = getFolder(id);
		if (realUploadPath == null ||realUploadPath.getChildren() == -1) {
			return Result.Error;
		}

		*//*
			判断权限
		 *//*
		User user = userMapper.selectByPrimaryKey(realUploadPath.getUserId());
		boolean privilege = ConfirmUtil.confirmCurrentUser(currentUser, user);
		if (!privilege) throw  new CustomRuntimeException("你没有权限访问!!");

		//获取multiRequest 中所有的文件名
		Iterator iter=multiRequest.getFileNames();

		while(iter.hasNext()){
			//一次遍历所有文件
			List<MultipartFile> files=multiRequest.getFiles(iter.next().toString());
			if(files!=null){
				for (MultipartFile multipartFile: files) {
					CommonsMultipartFile tempFile = (CommonsMultipartFile)multipartFile;
					FileItem fileItem = tempFile.getFileItem();
					String name = fileItem.getName();
					int end = name.length() - multipartFile.getOriginalFilename().length();
					String path = name.substring(0, end);
					String[] paths = path.split("/");
					//上传
					try {
						//根路径
						String rootFolder = "D:/temp/onkshare/"+ user.getUsername() + "/" + realUploadPath.getUrl();
						//目标路径
						java.io.File fileAndPath = new java.io.File(rootFolder + path);
						*//*
							如果路径不存在, 创建路径,并将每个文件夹信息存入数据库
						 *//*
						if (!fileAndPath.exists()){
							StringBuffer buffer = new StringBuffer();
							File folder1 = realUploadPath;
							for (String folderName: paths) {
								buffer.append(folderName + "/");
								java.io.File path1 = new java.io.File(rootFolder + buffer.toString());
								if (!path1.exists()) {
									if (folder1 == null) {
										throw new RuntimeException("错了!!");
									}
									File folder2 = new File();
									folder2.setParentId(folder1.getId());
									folder2.setUrl(realUploadPath.getUrl() + buffer.toString());
									folder2.setName(folderName);
									folder2.setUserId(user.getId());
									folder2.setChildren(0);
									folder2.setCreated(new Date());
									folder2.setUpdated(new Date());
									folder2.setLevel(folder1.getLevel() + 1);
									int flag = fileMapper.insert(folder2);

									path1.mkdir();

									File folder3 = getFolder(folder2.getName(), user.getId(), folder1.getId() );
									folder1 = folder3;
								}else {
									File folder = getFolder( realUploadPath.getUrl() + buffer.toString(), user.getId());
									folder1 = folder;
								}

							}
						}
						if(!path.endsWith("/")) {
							path = path + "/";
						}
						File currentFolder = getFolder( realUploadPath.getUrl() + path, user.getId());
						File file = new File();
						file.setParentId(currentFolder.getId());
						file.setUrl(currentFolder.getUrl() + multipartFile.getOriginalFilename());
						file.setName(multipartFile.getOriginalFilename());
						file.setUserId(user.getId());
						file.setChildren(-1);
						file.setCreated(new Date());
						file.setUpdated(new Date());
						file.setSize(multipartFile.getSize());
						//file.setType(multipartFile.getContentType());
						file.setLevel(currentFolder.getLevel() + 1);
						fileMapper.insert(file);
						multipartFile.transferTo(new java.io.File(fileAndPath + "/" + multipartFile.getOriginalFilename()));
					}catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}

		}
		return Result.SUCCESS;*/
	}

	@Override
	public Result deleteFile(Long[] ids, User currentUser) {

		try {
			for (Long id: ids) {
				/*
				判断权限
			 */
				File file = fileMapper.selectByPrimaryKey(id);
				if (file == null ) throw  new CustomRuntimeException("删除文件出错!!");
				User user = userMapper.selectByPrimaryKey(file.getUserId());
				boolean privilege = ConfirmUtil.confirmCurrentUser(currentUser, user);
				if (!privilege) throw  new CustomRuntimeException("你没有权限访问!!");

			/*
				再删除file
			 */

				if (file.getChildren() == -1) {
				/*
				先删除file详情
			 	*/
					FileDesc desc = fileDescMapper.selectByPrimaryKey(id);
					if (desc != null) fileDescMapper.deleteByPrimaryKey(id);


					int deleteF = fileMapper.deleteByPrimaryKey(file.getId());
					FileMapKey key = new FileMapKey();
					key.setStorageMethod(file.getStorageMethod());
					key.setStorageId(file.getStorageId());
					FileMap fileMap = fileMapMapper.selectByPrimaryKey(key);
					if (fileMap != null) {
						Long hold = fileMap.getHold();
						if (hold > 1) {
							fileMap.setHold(--hold);
							fileMapMapper.updateByPrimaryKey(fileMap);
						} else {
							fileMapMapper.deleteByPrimaryKey(fileMap);
							String path = fileMap.getPath();
							java.io.File filePath = new java.io.File(path);
							filePath.delete();
						}
					}
				}
				FileExample example = new FileExample();
				FileExample.Criteria criteria = example.createCriteria();
				criteria.andUrlLike(file.getUrl() + "%");
				List<File> files = fileMapper.selectByExample(example);
				for (File item : files) {
				/*
						先删除file详情
			 		*/
					FileDesc desc1 = fileDescMapper.selectByPrimaryKey(item.getId());
					if (desc1 != null) fileDescMapper.deleteByPrimaryKey(item.getId());

					if (item.getChildren() == -1) {
						fileMapper.deleteByPrimaryKey(item.getId());
						FileMapKey key1 = new FileMapKey();
						key1.setStorageMethod(item.getStorageMethod());
						key1.setStorageId(item.getStorageId());
						FileMap fileMap1 = fileMapMapper.selectByPrimaryKey(key1);
						if (fileMap1 != null){
							Long hold = fileMap1.getHold();
							if (hold > 1) {
								fileMap1.setHold(--hold);
								fileMapMapper.updateByPrimaryKey(fileMap1);
							}else {
								fileMapMapper.deleteByPrimaryKey(fileMap1);
								String path = fileMap1.getPath();
								java.io.File filePath = new java.io.File(path);
								filePath.delete();
							}
						}else {
							int b = fileMapper.deleteByPrimaryKey(item.getId());
						}
					}else {
						fileMapper.deleteByPrimaryKey(item.getId());
					}
				}

			/*
				更新文件状态
			 */
				deleteFileAfterChangeStatus(file.getParentId(), user);
			}

		}catch (Exception e) {
			e.printStackTrace();
			throw  new RuntimeException(e);
		}finally {

		}
		return Result.SUCCESS;
	}

	@Override
	public Result updateFile(Long id, String text, boolean folder, User currentUser) {
		File file = fileMapper.selectByPrimaryKey(id);

		if(file == null ) {
			return Result.Error;
		}
		/*
			权限判断
		 */
		User user = userMapper.selectByPrimaryKey(file.getUserId());
		boolean privilege = ConfirmUtil.confirmCurrentUser(currentUser, user);
		if (!privilege) throw  new CustomRuntimeException("你没有权限访问!!");



		String url = file.getUrl();
		String newUrl = text + "/";
		//java.io.File path = new java.io.File(WebConstant.ROOTFOLDER + user.getUsername() + "/" + url);
		/*
			单文件
		 */
		if (file.getChildren() == -1) {

			if (url.contains("/")) {
				int point = url.lastIndexOf("/");
				file.setUrl(url.substring(0, point + 1)  + text);
			}else {
				file.setUrl(text);
			}
			file.setName(text);
			fileMapper.updateByPrimaryKey(file);
			newUrl = file.getUrl();
		}else {
			/*
				文件夹
			 */
			//截取path 如 "操作系统-myself/计算机网络/" -->> 操作系统-myself/ ,文件夹"计算机网络/"是需要被替换的
			if (url.substring(0,url.length()).contains("/")) {
				String tempUrl = url.substring(0, url.lastIndexOf("/"));
				newUrl = tempUrl.substring(0, tempUrl.lastIndexOf("/") + 1) + text + "/";
			}
			file.setName(text);
			file.setUrl(newUrl);
			fileMapper.updateByPrimaryKey(file);

			FileExample example = new FileExample();
			FileExample.Criteria criteria = example.createCriteria();
			criteria.andUrlLike(url + "%");
			List<File> files = fileMapper.selectByExample(example);
			for (File item: files) {
				String oldPath = item.getUrl();
				String newPath = newUrl + oldPath.substring(url.length());
				item.setUrl(newPath);
				fileMapper.updateByPrimaryKey(item);
			}
		}

		/*if (!path.exists()) {
			throw new RuntimeException("需更新文件已不存在!!");
		}
		boolean update = path.renameTo(new java.io.File(WebConstant.ROOTFOLDER + user.getUsername() + "/" + newUrl));
		if (!update) {
			throw new RuntimeException("更新文件失败!!");
		}*/
		return Result.SUCCESS;
	}


	@Override
	public FileDesc getFileDesc(Long id) {
		try {
			FileDesc fileDesc = fileDescMapper.selectByPrimaryKey(id);
			return fileDesc;
		}catch (Exception e) {
			e.printStackTrace();
			throw new CustomRuntimeException("获取文件详情失败!!");
		}
	}

	@Override
	public Result editDetailsAndBrief(Long id, String desc, String attraction, User currentUser) {

		/*
			判断权限
		 */
		File file = fileMapper.selectByPrimaryKey(id);
		Long userId = file.getUserId();
		User user = userMapper.selectByPrimaryKey(userId);
		boolean privilege = ConfirmUtil.confirmCurrentUser(currentUser, user);
		if (!privilege) throw  new CustomRuntimeException("你没有权限访问!!");

		try {

			/*
				修改文件简述
			 */
			file.setAttraction(attraction);
			fileMapper.updateByPrimaryKey(file);

			/*
				修改文件详情
			 */

			FileDesc fileDesc = fileDescMapper.selectByPrimaryKey(id);
			if (fileDesc == null ) {
				fileDesc = new FileDesc();
				fileDesc.setCreated(new Date());
				fileDesc.setUpdated(new Date());
				fileDesc.setFileDesc(desc);
				fileDesc.setFileId(id);
				int flag = fileDescMapper.insert(fileDesc);
				if (flag == 0) throw new CustomRuntimeException("添加详情失败!!");
				return  Result.SUCCESS;
			}else {
				FileDesc details = new FileDesc();
				details.setCreated(fileDesc.getCreated());
				details.setFileId(fileDesc.getFileId());
				details.setUpdated(new Date());
				details.setFileDesc(desc);
				int flag = fileDescMapper.updateByPrimaryKeyWithBLOBs(details);
				if (flag == 0) throw new CustomRuntimeException("更新详情失败!!");
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new CustomRuntimeException("editDetails出异常!!");
		}
		return Result.SUCCESS;
	}

	@Override
	public List getShareList(User target, User current) {
		List reList = new ArrayList();

		Map conditions = new HashMap();
		conditions.put("userId", target.getId());
		conditions.put("status", 1);
		Integer level = fileMapper.selectFileMaxLevelByUserIdAndStatus(conditions);

		conditions.clear();
		for (int i = level; i > 0; i--) {
			conditions.put("userId", target.getId());
			conditions.put("level", i);
			conditions.put("status", 1);
			List<File> files = fileMapper.selectLevelFileExamplesByUserIdAndLevelAndStatus(conditions);
			//reList.add(files);
			for ( File item: files) {
				File file = fileMapper.selectByPrimaryKey(item.getParentId());
				if (file.getStatus() == 2) {
					FileExample example  = new FileExample();
					FileExample.Criteria criteria = example.createCriteria();
					criteria.andUserIdEqualTo(item.getUserId());
					criteria.andStatusEqualTo(item.getStatus());
					criteria.andLevelEqualTo(item.getLevel());
					criteria.andParentIdEqualTo(item.getParentId());
					List<File> temps = fileMapper.selectByExample(example);
					reList.addAll(temps);
					break;
				}
			}
		}
		Collections.reverse(reList);
		return reList;

		/*FileExample example = new FileExample();
		FileExample.Criteria criteria = example.createCriteria();
		byte b = 1;
		criteria.andUserIdEqualTo(target.getId());
		criteria.andStatusEqualTo(b);
		List<File> files = fileMapper.selectByExample(example);


			*//*提取folder, file*//*

		List<File> folders = new ArrayList<>();
		List<File> tfiles = new ArrayList<>();
		for (File item: files) {
			if (item.getChildren() != -1) {
				folders.add(item);
			}else {
				tfiles.add(item);
			}
		}


			*//*构建文件关系*//*

		for (File item: folders) {
			for ( File fileItem: tfiles) {
				if (fileItem.getParentId().equals(item.getId())) {
					files.remove(fileItem);
				}
			}
		}
		for (File item: folders) {
			for (File inItem: folders) {
				if (inItem.getParentId().equals(item.getId())) {
					files.remove(inItem);
				}
			}
		}

		return files;*/
	}

	@Override
	public User findUserByFileId(Long id) {
		File file = fileMapper.selectByPrimaryKey(id);
		User user = userMapper.selectByPrimaryKey(file.getUserId());
		return user;
	}

	@Override
	public Result updateSharedFile(Long[] ids, Long[] unIds, Long[] reIndIds) {
		try {
			if (ids != null || ids.length != 0) {
				fileMapper.updateFileStatusToOneByIds(ids);
			}
			if (unIds != null || unIds.length != 0) {
				fileMapper.updateFileStatusToZeroByIds(unIds);
			}
			if (reIndIds != null || reIndIds.length != 0) {
				fileMapper.updateFileStatusToTwoByIds(reIndIds);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw  new CustomRuntimeException("更新分享文件失败!!");
		}
		return Result.SUCCESS;
	}


	@Override
	public Result moveFile(Long parentId, Long sourceId, User currentUser) {


		File folder = fileMapper.selectByPrimaryKey(parentId);
		if (folder == null) {
			throw new CustomRuntimeException("文件夹已不存在!!");
		}
		if (folder.getChildren() == -1) {
			throw  new CustomRuntimeException("你想移动到文件中,没门!!");
		}

		/*
			权限判断
		 */

		Long userId = folder.getUserId();
		User user = userMapper.selectByPrimaryKey(userId);
		boolean privilege = ConfirmUtil.confirmCurrentUser(currentUser, user);
		if (!privilege) throw  new CustomRuntimeException("你没有权限访问!!");


		File source = fileMapper.selectByPrimaryKey(sourceId);
		String oldPath = new String(source.getUrl());
		java.io.File sourcePath = new java.io.File(WebConstant.ROOTFOLDER + user.getUsername() + "/"+ oldPath);
		java.io.File targetPath = new java.io.File(WebConstant.ROOTFOLDER + user.getUsername() + "/"+ folder.getUrl() + source.getName());
		if (sourcePath.getPath().equals(targetPath.getPath())) {
			return Result.SUCCESS;
		}
		forMoveFile(source, folder);
		deleteFileAfterChangeStatus(source.getParentId(), user);


		/*
			移动o文件
		 */

		/*try {

			Path move = Files.move(sourcePath.toPath(), targetPath.toPath(), StandardCopyOption.ATOMIC_MOVE);
			//if (move == null) throw new CustomRuntimeException("移动失败!!");
		}catch (Exception e) {
			e.printStackTrace();
			throw new CustomRuntimeException("移动失败!!");
		}*/

		return Result.SUCCESS;
	}

	@Override
	public String downloadFile(Long id, User currentUser) {

		File file = fileMapper.selectByPrimaryKey(id);

		if (file.getStatus() != 1) {
			/*
			权限判断
		 */
			Long userId = file.getUserId();
			User user = userMapper.selectByPrimaryKey(userId);
			boolean privilege = ConfirmUtil.confirmCurrentUser(currentUser, user);
			if (!privilege) throw  new CustomRuntimeException("你没有权限访问!!");
		}

		FileMapKey key = new FileMapKey();
		key.setStorageId(file.getStorageId());
		key.setStorageMethod(file.getStorageMethod());
		FileMap fileMap = fileMapMapper.selectByPrimaryKey(key);
		return fileMap.getPath();
	}


	private void forMoveFile(File target, File newParent) {
		if (newParent.getChildren() == -1) throw  new CustomRuntimeException("什么贵!!");
		if (target.getChildren() == -1) {
			target.setLevel(newParent.getLevel() + 1 );
			target.setParentId(newParent.getId());
			target.setUrl(newParent.getUrl() + target.getName());
			switch (newParent.getStatus()) {
				case 0:
				case 1:target.setStatus(newParent.getStatus()); break;
				default:break;
			}
			fileMapper.updateByPrimaryKey(target);
			return;
		}else {
			target.setLevel(newParent.getLevel() + 1);
			target.setParentId(newParent.getId());
			target.setUrl(newParent.getUrl() + target.getName() + "/");
			switch (newParent.getStatus()) {
				case 0:
				case 1:target.setStatus(newParent.getStatus()); break;
				default:break;
			}
			fileMapper.updateByPrimaryKey(target);
			Map map = new HashMap();
			map.put("userId", target.getUserId());
			map.put("parentId", target.getId());
			List<File> files = fileMapper.selectFileByUserIdAndParentId(map);
			if (files == null) return;

			for (File file: files) {
					forMoveFile(file, target);
			}
		}

		return;
	}

	private void forCopyFile(File target, File newParent) {
		if (target == null) return;
		if (newParent.getChildren() == -1) throw  new CustomRuntimeException("什么贵!!");
		if (target.getChildren() == -1) {
			File temp = new File();
			temp.setCreated(new Date());
			temp.setUpdated(new Date());
			temp.setAttraction(target.getAttraction());
			temp.setStorageId(target.getStorageId());
			temp.setStorageMethod(target.getStorageMethod());
			temp.setStatus(newParent.getStatus());
			temp.setChildren(-1);
			temp.setExtra(target.getExtra());
			temp.setLevel(newParent.getLevel()+ 1);
			temp.setName(target.getName());
			temp.setParentId(newParent.getId());
			temp.setSize(target.getSize());
			temp.setType(target.getType());
			byte b = 0;
			temp.setStatus( newParent.getStatus() != 1 ? b : 1 );
			temp.setUrl(newParent.getUrl() + target.getName());
			temp.setUserId(newParent.getUserId());
			temp.setWorth(target.getWorth());
			fileMapper.insert(temp);
		}else {
			File temp = new File();
			temp.setCreated(new Date());
			temp.setUpdated(new Date());
			temp.setAttraction(target.getAttraction());
			temp.setStorageId(target.getStorageId());
			temp.setStorageMethod(target.getStorageMethod());
			temp.setStatus(newParent.getStatus());
			temp.setChildren(0);
			temp.setExtra(target.getExtra());
			temp.setLevel(newParent.getLevel()+ 1);
			temp.setName(target.getName());
			temp.setParentId(newParent.getId());
			temp.setSize(target.getSize());
			temp.setType(target.getType());
			byte b = 0;
			temp.setStatus( newParent.getStatus() != 1 ? b : 1 );
			temp.setUrl(newParent.getUrl() + target.getName() + "/");
			temp.setUserId(newParent.getUserId());
			temp.setWorth(target.getWorth());
			Long id = fileMapper.insert(temp);

			Map map = new HashMap();
			map.put("userId", target.getUserId());
			map.put("parentId", target.getId());
			List<File> files = fileMapper.selectFileByUserIdAndParentId(map);
			//File f = fileMapper.selectByPrimaryKey(id);
			for (File file : files) {
				forCopyFile(file, temp);
			}
		}
	}

	public void deleteFileAfterChangeStatus(Long id, User user) {
		File target = fileMapper.selectByPrimaryKey(id);
		if (target == null) return;
		Map map = new HashMap();
		map.put("userId", user.getId());
		map.put("parentId", target.getId());
		List<File> files = fileMapper.selectFileByUserIdAndParentId(map);

		/*
			如果删除后文件夹为空
		 */
		if (files.isEmpty() && target.getStatus() == 2) {
			byte b = 0;
			target.setStatus(b);
			fileMapper.updateByPrimaryKey(target);
			deleteFileAfterChangeStatus(target.getId(), user);
			return ;
		}

		boolean status0Count = false;
		boolean status1Count = false;
		boolean status2Count = false;
		for (File file: files) {
			byte b = file.getStatus();
			if (b == 0) status0Count = true;
			if (b == 1) status1Count = true;
			if (b == 2) status2Count = true;
		}
		/*
			存在子文件夹,并status为2
		 */
		if (status2Count) {
			return;
		}
		/*
			存在文件status的0,1状态共存
		 */
		if (status0Count && status1Count) {
			return;
		}
		/*
			存在文件status的0或1状态
		 */
		if (status0Count && !status1Count && target.getStatus() == 2) {
			byte b = 0;
			target.setStatus(b);
			fileMapper.updateByPrimaryKey(target);
			deleteFileAfterChangeStatus(target.getParentId(), user);
			return;
		}
		if (!status0Count && status1Count && target.getStatus() == 2) {
			byte b = 1;
			target.setStatus(b);
			fileMapper.updateByPrimaryKey(target);
			deleteFileAfterChangeStatus(target.getParentId(), user);
		}

		return;
	}

	@Override
	public Result copyFiles(Long[] ids, Long folderId, User currentUser) {
		File folder = fileMapper.selectByPrimaryKey(folderId);
		if (folder == null || folder.getChildren() == -1) {
			throw new CustomRuntimeException("copy exception!!");
		}
		for (Long id: ids) {
			File file = fileMapper.selectByPrimaryKey(id);
			forCopyFile(file, folder);
		}

		return Result.SUCCESS;
	}
}
