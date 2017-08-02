package mycourse.onkshare.manager.service.impl;

import mycourse.onkshare.common.util.ConfirmUtil;
import mycourse.onkshare.common.util.IOUtil;
import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.constant.e.StorageOption;
import mycourse.onkshare.encoder.Encoder;
import mycourse.onkshare.exception.CustomRuntimeException;
import mycourse.onkshare.manager.service.FileService;
import mycourse.onkshare.manager.service.SitesMessageService;
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

@Service("sitesMessageService")
public class SitesMessageServiceImpl implements SitesMessageService {


	@Resource
	SitesMessageMapper sitesMessageMapper;
	@Resource
	UserMapper userMapper;

	@Override
	public Result getMessage(Long parentId, Integer startPage, Integer pageSize) {

		Map conditions = new HashMap();
		conditions.put("startPage", (startPage -1)* pageSize);
		conditions.put("endPage", (startPage -1)* pageSize + pageSize);
		List<SitesMessage> sitesMessages = sitesMessageMapper.selectMessage(conditions);

		for (SitesMessage sitesMessage: sitesMessages) {
			Long userId = sitesMessage.getUserId();
			User user = userMapper.selectByPrimaryKey(userId);
			sitesMessage.setName(user.getUsername());
		}

		PagingResult result = new PagingResult();
		result.setPageNumber(startPage);
		result.setPageSize(pageSize);
		result.setRows(sitesMessages);
		SitesMessageExample example = new SitesMessageExample();
		SitesMessageExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		long count = sitesMessageMapper.countByExample(example);
		result.setTotal(count);
		result.setMessage("success");
		return result;
	}

	@Override
	public Result appendMessage(Long parentId, String content, Long floor,  User current) {
		if (parentId == 0) {
			SitesMessage sitesMessage = new SitesMessage();
			sitesMessage.setLevel(0);
			sitesMessage.setParentId(0L);
			sitesMessage.setUserId(current.getId());
			sitesMessage.setText(content);

			/*
				设置floor
			 */
			Long fm = sitesMessageMapper.selectMessageFloorMax();
			sitesMessage.setFloor(fm + 1);

			sitesMessage.setCreated(new Date());
			sitesMessageMapper.insert(sitesMessage);
			SitesMessageExample example = new SitesMessageExample();
			SitesMessageExample.Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(current.getId());
			criteria.andParentIdEqualTo(parentId);
			criteria.andTextEqualTo(content);
			List<SitesMessage> sitesMessages = sitesMessageMapper.selectByExample(example);
			SitesMessage sitesMessage1 = sitesMessages.get(0);
			User user = userMapper.selectByPrimaryKey(sitesMessage1.getUserId());
			sitesMessage1.setName(user.getUsername());
			ObjectResult result = new ObjectResult();
			result.setObject(sitesMessage1);
			return result;
		}
		SitesMessage parentMessage = sitesMessageMapper.selectByPrimaryKey(parentId);
		if (parentMessage == null) {
			throw new CustomRuntimeException("parentId没找到!!");
		}
		SitesMessage sitesMessage = new SitesMessage();
		sitesMessage.setLevel(parentMessage.getLevel()+1);
		sitesMessage.setParentId(parentMessage.getId());
		sitesMessage.setUserId(current.getId());
		sitesMessage.setText(content);
		sitesMessage.setFloor(floor);
		sitesMessage.setCreated(new Date());
		sitesMessageMapper.insert(sitesMessage);
		SitesMessageExample example = new SitesMessageExample();
		SitesMessageExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(current.getId());
		criteria.andParentIdEqualTo(parentId);
		criteria.andTextEqualTo(content);
		List<SitesMessage> sitesMessages = sitesMessageMapper.selectByExample(example);
		SitesMessage sitesMessage1 = sitesMessages.get(0);
		User user = userMapper.selectByPrimaryKey(sitesMessage1.getUserId());
		sitesMessage1.setName(user.getUsername());
		ObjectResult result = new ObjectResult();
		result.setObject(sitesMessage1);
		return result;
	}
}
