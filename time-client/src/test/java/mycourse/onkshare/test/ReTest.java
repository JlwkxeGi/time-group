package mycourse.onkshare.test;

import mycourse.onkshare.common.util.IOUtil;
import mycourse.onkshare.exception.CustomRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import sun.misc.Regexp;
import sun.misc.RegexpTarget;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by doll on 3/2.
 */
public class ReTest {

    @Test
    public void test() {

        String url = "ads(234)daf(124) (123).tet";
        String rename = IOUtil.rename(url);
        System.out.println(rename);
    }

    @Test
    public void testIO() {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            java.io.File file = new java.io.File("D:\\temp\\onkshare\\2017\\3\\03\\21\\2017-3-03-c4c18b0e598d4477-Reimu 灵梦.mp4");
            if (!file.exists()) throw new CustomRuntimeException("路径不存在!!");
            HttpHeaders headers = new HttpHeaders();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int available = bis.available();
            int position = 0;
            byte[] b = new byte[4096 * 2];
            do {
                position = bis.read(b);
            }while (position != -1);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (bis != null) {
                try {bis.close();}catch (Exception e){ e.printStackTrace();}
            }
        }
    }
}
