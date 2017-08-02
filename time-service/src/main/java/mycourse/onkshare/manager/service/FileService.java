package mycourse.onkshare.manager.service;


import mycourse.onkshare.model.File;
import mycourse.onkshare.model.FileDesc;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import mycourse.onkshare.model.User;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

public interface FileService {

  /* public Number getFileCount();*/

   public List<File> getFileList(Long parentId, User user);

   public List<File> getShareFileList(Long parentId);

   public File getFolder(String folderName ,Long userId, Long parentId);

   public File getFolder(String url ,Long userId);

   public File getFile(Long id);

   public PagingResult getUsersTopFolderPagingList(Integer startPage, Integer pageSize, User user);

   //ublic List<FileCat> getCatList(Long parentId);

   public Result addFile(MultipartFile[] uploadFile, User user, File filepath);

   File addFolder(Long id, String folder, String attraction, User user);

   Result uploadFolder(MultipartHttpServletRequest multiRequest, User user);

   Result deleteFile(Long[] id,  User user);

   Result updateFile(Long id ,String text, boolean folder,  User user);

   FileDesc getFileDesc(Long id);

   Result editDetailsAndBrief(Long id , String desc, String attraction, User user);

   List getShareList(User target, User current);

   User findUserByFileId(Long id);

   List getFileListByUser(User target, User current);

   Result updateSharedFile(Long[] ids, Long[] unId, Long[] reIndIds);

   Result moveFile(Long parentId, Long targetId, User currentUser);

   String downloadFile(Long id, User currentUser);

   Result copyFiles(Long[] ids, Long folderId, User currentUser);

}
