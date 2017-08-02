package mycourse.onkshare.encoder;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * Created by doll on 3/1.
 */
public class Md5AndSizeEncoder extends Encoder {

    public static final String METHOD = "md5_size";

    @Override
    public boolean getResult(File target1, File target2) {
        String target1Md5 = getFileIdentify(target1);
        String target2Md5 = getFileIdentify(target2);
        if (target1Md5.equals(target2Md5) && target1.length() == target2.length()) {
            return true;
        }
        return false;
    }

    public String  getMethodName() {
        return METHOD;
    }

    public String getFileIdentify(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            MappedByteBuffer mappedByteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 =  MessageDigest.getInstance("MD5");
            md5.update(mappedByteBuffer);
            BigInteger bigInteger = new BigInteger(1, md5.digest());
            String s = bigInteger.toString(16);
            return s;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFileIdentify(FileInputStream fis) {
        try {
            MappedByteBuffer mappedByteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fis.available());
            MessageDigest md5 =  MessageDigest.getInstance("MD5");
            md5.update(mappedByteBuffer);
            BigInteger bigInteger = new BigInteger(1, md5.digest());
            String s = bigInteger.toString(16);
            return s;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFileIdentify(ByteBuffer byteBuffer) {
        try {
            MessageDigest md5 =  MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bigInteger = new BigInteger(1, md5.digest());
            String s = bigInteger.toString(16);
            return s;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
