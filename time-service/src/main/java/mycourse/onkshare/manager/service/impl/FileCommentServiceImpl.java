package mycourse.onkshare.manager.service.impl;

import mycourse.onkshare.common.util.ConfirmUtil;
import mycourse.onkshare.common.util.IOUtil;
import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.constant.e.StorageOption;
import mycourse.onkshare.encoder.Encoder;
import mycourse.onkshare.exception.CustomRuntimeException;
import mycourse.onkshare.manager.service.FileCommentService;
import mycourse.onkshare.manager.service.FileService;
import mycourse.onkshare.mapper.*;
import mycourse.onkshare.model.*;
import mycourse.onkshare.model.result.ObjectResult;
import mycourse.onkshare.model.result.PagingResult;
import mycourse.onkshare.model.result.Result;
import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;

@Service("fileCommentService")
public class FileCommentServiceImpl implements FileCommentService {

	@Resource
	FileCommentMapper fileCommentMapper;
	@Resource
	UserMapper userMapper;
	@Resource
	FileMapper fileMapper;

	@Override
	public PagingResult getFileComments(Long fileId, Long parentId, Integer startPage, Integer pageSize) {

		File file = fileMapper.selectByPrimaryKey(fileId);
		if (file == null) {
			throw new CustomRuntimeException("fileId为空!!");
		}

		Map conditions = new HashMap();
		conditions.put("startPage", (startPage -1)* pageSize);
		conditions.put("endPage", (startPage -1)* pageSize + pageSize);
		conditions.put("fileId", fileId);
		List<FileComment> fileComments = fileCommentMapper.selectFileComment(conditions);

		for (FileComment fileComment: fileComments) {
			Long userId = fileComment.getUserId();
			User user = userMapper.selectByPrimaryKey(userId);
			fileComment.setName(user.getUsername());
		}

		PagingResult result = new PagingResult();
		result.setPageNumber(startPage);
		result.setPageSize(pageSize);
		result.setRows(fileComments);
		FileCommentExample example = new FileCommentExample();
		FileCommentExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		long count = fileCommentMapper.countByExample(example);
		result.setTotal(count);
		result.setMessage("success");
		return result;
	}

	@Override
	public ObjectResult appendFileComment(Long fileId, Long parentId, String content, Long floor,  User current) {

		File file = fileMapper.selectByPrimaryKey(fileId);
		if (file == null) {
			throw new CustomRuntimeException("fileId为空!!");
		}

		if (parentId == 0) {
			FileComment fileComment = new FileComment();
			fileComment.setLevel(0L);
			fileComment.setParentId(0L);
			fileComment.setUserId(current.getId());
			fileComment.setText(content);
			fileComment.setStatus(0);
			fileComment.setFileId(fileId);
			/*
				设置floor
			 */
			Long fm = fileCommentMapper.selectFileCommentFloorMax(fileId);
			fileComment.setFloor(fm + 1);

			fileComment.setCreated(new Date());
			fileCommentMapper.insert(fileComment);
			FileCommentExample example = new FileCommentExample();
			FileCommentExample.Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(current.getId());
			criteria.andParentIdEqualTo(parentId);
			criteria.andTextEqualTo(content);
			List<FileComment> fileComments = fileCommentMapper.selectByExample(example);
			FileComment fileComment1 = fileComments.get(0);
			User user = userMapper.selectByPrimaryKey(fileComment1.getUserId());
			fileComment1.setName(user.getUsername());
			ObjectResult result = new ObjectResult();
			result.setObject(fileComment1);
			return result;
		}
		FileComment parentFileComment = fileCommentMapper.selectByPrimaryKey(parentId);
		if (parentFileComment == null) {
			throw new CustomRuntimeException("parentId没找到!!");
		}
		FileComment fileComment = new FileComment();
		fileComment.setLevel(parentFileComment.getLevel()+1);
		fileComment.setParentId(parentFileComment.getId());
		fileComment.setUserId(current.getId());
		fileComment.setText(content);
		fileComment.setFloor(floor);
		fileComment.setFileId(fileId);
		fileComment.setStatus(0);
		fileComment.setCreated(new Date());
		fileCommentMapper.insert(fileComment);
		FileCommentExample example = new FileCommentExample();
		FileCommentExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(current.getId());
		criteria.andParentIdEqualTo(parentId);
		criteria.andTextEqualTo(content);
		List<FileComment> fileComments = fileCommentMapper.selectByExample(example);
		FileComment fileComment1 = fileComments.get(0);
		User user = userMapper.selectByPrimaryKey(fileComment1.getUserId());
		fileComment1.setName(user.getUsername());
		ObjectResult result = new ObjectResult();
		result.setObject(fileComment1);
		return result;
	}
}
