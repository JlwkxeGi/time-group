package mycourse.onkshare.api.service;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by ww on 2017/2/5.
 */
public interface PictureService {

    public List<String> savePicture(MultipartHttpServletRequest uploadFiles);
}
