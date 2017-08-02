package mycourse.onkshare.manager.service.impl;

import mycourse.onkshare.common.util.ConfirmUtil;
import mycourse.onkshare.common.util.IOUtil;
import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.constant.e.StorageOption;
import mycourse.onkshare.dao.FileRedisDao;
import mycourse.onkshare.exception.CustomRuntimeException;
import mycourse.onkshare.encoder.Md5AndSizeEncoder;
import mycourse.onkshare.manager.service.FileRedisService;
import mycourse.onkshare.mapper.FileMapper;
import mycourse.onkshare.mapper.UserMapper;
import mycourse.onkshare.model.File;
import mycourse.onkshare.model.FileExample;
import mycourse.onkshare.model.User;
import mycourse.onkshare.model.result.Result;
import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@Service("fileRedisService")
public class FileRedisServiceImpl implements FileRedisService {

    @Resource
    FileMapper fileMapper;
    @Resource
    UserMapper userMapper;

    @Resource
    FileRedisDao fileRedisDao;


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
                    if (paths.length >= 1) {
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
                        InputStream is = multipartFile.getInputStream();
                        if (is instanceof FileInputStream) {
                            String fileMd5 = IOUtil.getFileMd5((FileInputStream) is, multipartFile.getSize());
                            String identify = fileMd5 + "_" + multipartFile.getSize();
                            String filePath = fileRedisDao.getFilePath(identify);
                            if (filePath == null || filePath.length() == 0) {
                            /*
                            * */
                                String byTimeUuid = IOUtil.getFileNameByTimeUuid(multipartFile.getOriginalFilename());
                                String rootPathByTime = IOUtil.getRootPathByTime(WebConstant.ROOTFOLDER, StorageOption.HOUR);
                                boolean flag = fileRedisDao.setFilePath(identify, rootPathByTime + "/" + byTimeUuid);
                                if (!flag) throw new CustomRuntimeException("文件上传出错!!");
                                java.io.File fPath = new java.io.File(rootPathByTime);
                                if (!fPath.exists()) {
                                    fPath.mkdirs();
                                }
                                java.io.File finalPath = new java.io.File(rootPathByTime + byTimeUuid);
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
                            file.setStorageMethod(Md5AndSizeEncoder.METHOD);
                            //file.setType(multipartFile.getContentType());
                            file.setLevel(parentFolder.getLevel() + 1);
                            fileMapper.insert(file);
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }

        }
        return Result.SUCCESS;
    }

    @Override
    public Result removeFile(Long id , User currentUser) {
        File file = fileMapper.selectByPrimaryKey(id);
        if (file == null || file.getId() == null) {
            return  Result.Error;
        }

        File selectFile = fileMapper.selectByPrimaryKey(file.getId());
        if (selectFile == null) Result.error("文件不存在!!");
        if (selectFile.getChildren() == -1) {
            boolean b = fileRedisDao.deleteFile(selectFile.getStorageId());
            if (b) return Result.SUCCESS;
            return  Result.Error;
        }
        FileExample example = new FileExample();
        FileExample.Criteria criteria = example.createCriteria();
        criteria.andUrlLike(file.getUrl() + "%");
        List<File> files = fileMapper.selectByExample(example);
        for (File item : files) {
            if (item.getChildren() == -1) {
                boolean b = fileRedisDao.deleteFile(item.getStorageId());
                if (!b) return Result.Error;
            }else {
                int b = fileMapper.deleteByPrimaryKey(item.getId());
                if (b == 0) return Result.Error;
            }
        }

        return Result.SUCCESS;
    }

    public File getFolder(String folderName , Long userId, Long parentId) {
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
}
