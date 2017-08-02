package mycourse.onkshare.common.util;

import mycourse.onkshare.constant.e.StorageOption;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by doll on 2017/2/15.
 */
public class IOUtil {

    public  static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public  static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    public static String getRootPathByTime(String root, StorageOption option) {
            if (root == null) throw new ClassCastException("root为空!");
            String format = null;
            switch (option) {
                case DAY:
                    format = DateFormatUtils.format(new Date(), "yyyy/M/dd");
                    break;
                case HOUR:
                    format = DateFormatUtils.format(new Date(), "yyyy/M/dd/HH");
                    break;
                case MINUTE:
                    format = DateFormatUtils.format(new Date(), "yyyy/M/dd/HH/mm");
                    break;
                case SECOND:
                    format = DateFormatUtils.format(new Date(), "yyyy/M/dd/HH/mm/ss");
                    break;
                default:
                    format = DateFormatUtils.format(new Date(), "yyyy/M/dd/HH");
                    break;
            }
            String path = root + "/" + format;
        return  path;
    }

    public static String  getFileNameByTimeUuid(String fileName) {
        UUID uuid = UUID.randomUUID();
        String id64 = uuid.toString().replaceAll("-", "");
        String id32 = id64.substring(0, 16);
        String format = DateFormatUtils.format(new Date(), "yyyy-M-dd");
        String name = format + "-" + id32 + "-" + fileName;
        return name;
    }

    public static String getFileMd5(File file) {
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

    public static String getFileMd5(FileInputStream fis, long size) {
        try {
            MappedByteBuffer mappedByteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, size);
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

    public static String rename (String name) {
        int i = name.lastIndexOf(".");
        String url = "";
        if (i != -1) {
            url =name.substring(0, i);
        }else {
            i = name.length();
            url = name;
        }

        Pattern pattern = Pattern.compile("\\(\\d+\\)$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String n = matcher.group(0);
            Long number = Long.valueOf(n.replace("(", "").replace(")", ""));
            String re = url.replaceAll("\\(\\d+\\)$", "(" + (++number) + ")");
            return re + name.substring(i);
        }else {
            return  url + "(1)" + name.substring(i);
        }
    }

    public static String rename (String name, Integer number) {
        if (number <= 0) {
            return name;
        }
        int i = name.lastIndexOf(".");
        String url = "";
        if (i != -1) {
            url =name.substring(0, i);
        }else {
            i = name.length();
            url = name;
        }

        Pattern pattern = Pattern.compile("\\(\\d+\\)$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String n = matcher.group(0);
            String re = url.replaceAll("\\(\\d+\\)$", "(" + number + ")");
            return re + name.substring(i);
        }else {
            return  url + "("  + number +")" + name.substring(i);
        }
    }

    public static String originalName (String name) {
        int i = name.lastIndexOf(".");
        String url = "";
        if (i != -1) {
            url =name.substring(0, i);
        }else {
            i = name.length();
            url = name;
        }

        Pattern pattern = Pattern.compile("\\(\\d+\\)$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String n = matcher.group(0);
            String re = url.replaceAll("\\(\\d+\\)$", "");
            return re + name.substring(i);
        }else {
            return  url + name.substring(i);
        }
    }

    public static Integer getFileCopyNameNumber(String name) {
        int i = name.lastIndexOf(".");
        String url = "";
        if (i != -1) {
            url =name.substring(0, i);
        }else {
            i = name.length();
            url = name;
        }

        Pattern pattern = Pattern.compile("\\(\\d+\\)$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String n = matcher.group(0);
            int number = Integer.valueOf(n.replace("(", "").replace(")", ""));
            return number;
        }
        return 0;
    }

    public static byte[] FileToByte(String path) {
        File file = new File(path);
        return null;
    }

}
