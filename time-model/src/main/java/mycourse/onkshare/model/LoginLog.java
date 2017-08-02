package mycourse.onkshare.model;

import java.util.Date;

public class LoginLog {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_login_log.id
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_login_log.user_id
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_login_log.user_name
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_login_log.login_address
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    private String loginAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_login_log.login_zone
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    private String loginZone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_login_log.login_time
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    private Date loginTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_login_log.status
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    private Integer status;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_login_log.id
     *
     * @return the value of tb_login_log.id
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_login_log.id
     *
     * @param id the value for tb_login_log.id
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_login_log.user_id
     *
     * @return the value of tb_login_log.user_id
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_login_log.user_id
     *
     * @param userId the value for tb_login_log.user_id
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_login_log.user_name
     *
     * @return the value of tb_login_log.user_name
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_login_log.user_name
     *
     * @param userName the value for tb_login_log.user_name
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_login_log.login_address
     *
     * @return the value of tb_login_log.login_address
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public String getLoginAddress() {
        return loginAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_login_log.login_address
     *
     * @param loginAddress the value for tb_login_log.login_address
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public void setLoginAddress(String loginAddress) {
        this.loginAddress = loginAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_login_log.login_zone
     *
     * @return the value of tb_login_log.login_zone
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public String getLoginZone() {
        return loginZone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_login_log.login_zone
     *
     * @param loginZone the value for tb_login_log.login_zone
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public void setLoginZone(String loginZone) {
        this.loginZone = loginZone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_login_log.login_time
     *
     * @return the value of tb_login_log.login_time
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_login_log.login_time
     *
     * @param loginTime the value for tb_login_log.login_time
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_login_log.status
     *
     * @return the value of tb_login_log.status
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_login_log.status
     *
     * @param status the value for tb_login_log.status
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}