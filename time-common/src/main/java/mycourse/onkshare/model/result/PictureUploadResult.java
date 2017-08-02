package mycourse.onkshare.model.result;

import java.util.List;

/**
 * Created by ww on 2017/2/5.
 */
public class PictureUploadResult extends Result {

    //上传成功时返回前台的PictureUrl
    private List<String> url;

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }
}
