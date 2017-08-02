package mycourse.onkshare.common.util;

import sun.net.ftp.FtpReplyCode;
import sun.net.ftp.impl.FtpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * Created by ww on 2017/2/5.
 */
public class FtpUtil {


    public static boolean updateFile(File file , String filename, String root,
                                     String address, int port,
                                     String username, String password) {
        if(file == null) {
            return false;
        }
        InputStream is = null;
        sun.net.ftp.FtpClient ftpClient = FtpClient.create();
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        try {
            ftpClient.connect(socketAddress);
            ftpClient.login(username, password.toCharArray());
            FtpReplyCode lastReplyCode = ftpClient.getLastReplyCode();
            if (!lastReplyCode.isPositiveCompletion()) {
                ftpClient.close();
                return false;
            }
            ftpClient.changeDirectory(root);
            ftpClient.setBinaryType();
            is = new FileInputStream(file);
            ftpClient.appendFile(filename, is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
               try {
                   ftpClient.close();
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
            if (is != null) {
                try {
                    is.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static boolean updateFile(InputStream is , String path,String filename,
                                     String address, int port,
                                     String username, String password) {

        if (is == null) {
            return false;
        }

        sun.net.ftp.FtpClient ftpClient = FtpClient.create();
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        try {
            ftpClient.connect(socketAddress);
            ftpClient.login(username, password.toCharArray());
            FtpReplyCode lastReplyCode = ftpClient.getLastReplyCode();
            if (!lastReplyCode.isPositiveCompletion()) {
                ftpClient.close();
                return false;
            }
            ftpClient.changeDirectory(path);
            ftpClient.setBinaryType();
            ftpClient.appendFile(filename, is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
