package mycourse.onkshare.api.web;

import mycourse.onkshare.constant.WebConstant;
import mycourse.onkshare.api.service.PictureService;
import mycourse.onkshare.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ww on 2017/2/5.
 */
@Controller
public class PictureController extends BaseController {

    @Resource
    private PictureService pictureService;


    @RequestMapping("/picture/upload")
    @ResponseBody
    public Map updatePicture(HttpServletRequest request) {
        Map map = new HashMap();
        User user = (User)request.getSession().getAttribute(WebConstant.LOGINUSER);
        if (user == null) {
            map.put("error", 1);
            map.put("message", "请先登录!!");
            return map;
        }

        String dir = request.getParameter("dir");

        long  startTime=System.currentTimeMillis();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            List<String> result = pictureService.savePicture(multiRequest);


            if (result == null) {
                map.put("error", 1);
                map.put("message", "上传失败!!");
                return map;
            }

            if ("image".equals(dir)){
                map.put("error", 0);
                map.put("url", result.get(0));
            }
            if ("nultiimage".equals(dir)){
                map.put("error", 0);
                map.put("url", result);
            }

            long  endTime=System.currentTimeMillis();
            System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
            return map;
        }
        return  null;
    }
}
