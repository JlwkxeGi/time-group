package mycourse.onkshare.constant;

import mycourse.onkshare.common.util.PropertyConfigurer;

/**
 * Created by ww on 2017/1/30.
 */
public  final class WebConstant {

    public static final String LOGINUSER = "loginUser";

    public static final PropertyConfigurer CONFIGURER = new PropertyConfigurer();
    public static final String BASEPATH = CONFIGURER.getProperty("basepath");
    public static final String CSSPATH = CONFIGURER.getProperty("csspath");
    public static final String IMAGEPATH = CONFIGURER.getProperty("imagepath");


    public static final String FTP_ADDRESS = CONFIGURER.getProperty("ftpaddress");
    public static final Integer FTP_PORT = Integer.valueOf(CONFIGURER.getProperty("ftpport"));
    public static final String FTP_BASE_PATH = CONFIGURER.getProperty("ftpbasepath");
    public static final String FTP_PICTURE_PATH = CONFIGURER.getProperty("ftppictruePath");
    public static final String FTP_USERNAME = CONFIGURER.getProperty("ftpusername");
    public static final String FTP_PASSWORD = CONFIGURER.getProperty("ftppassword");

    public static final String NGINX_ADDRESS = CONFIGURER.getProperty("nginxaddress");

    public static final String TEMPDIRTORY = CONFIGURER.getProperty("tempdirtory");

    public static final String ROOTFOLDER = CONFIGURER.getProperty("rootFolder");

    public static final String SEARCH_FILE_PATH = CONFIGURER.getProperty("search_file_path");
}
