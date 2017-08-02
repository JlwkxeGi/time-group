package mycourse.onkshare.api.service.impl;

import mycourse.onkshare.common.util.FtpUtil;
import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.model.result.PictureUploadResult;
import mycourse.onkshare.api.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ww on 2017/2/5.
 */
@Service("pictureService")
public class PictureServiceImpl implements PictureService {
    @Override
    public List<String> savePicture(MultipartHttpServletRequest multiRequest) {
        InputStream is = null;
        boolean flag = false;
        List<String> urls = new ArrayList<>();
        PictureUploadResult result = new PictureUploadResult();
        try {
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();

            while (iter.hasNext()) {
                //一次遍历所有文件
                List<MultipartFile> files = multiRequest.getFiles(iter.next().toString());
                if (files != null) {
                    for (MultipartFile multipartFile : files) {
                        CommonsMultipartFile tempFile = (CommonsMultipartFile) multipartFile;
                        is = multipartFile.getInputStream();
                        flag = FtpUtil.updateFile(is, WebConstant.FTP_PICTURE_PATH,
                                multipartFile.getOriginalFilename(), WebConstant.FTP_ADDRESS, WebConstant.FTP_PORT,
                                WebConstant.FTP_USERNAME, WebConstant.FTP_PASSWORD);
                        urls.add(WebConstant.NGINX_ADDRESS + WebConstant.FTP_PICTURE_PATH + multipartFile.getOriginalFilename());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (flag) {
                return  urls;
            }
            return null;
        }
    }
}
