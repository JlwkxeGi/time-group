package mycourse.onkshare.model;

import java.util.Date;

public class File {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.id
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.name
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private String name;

    private String storageMethod;

    private String storageId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.attraction
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private String attraction;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.worth
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private Long worth;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.type
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.size
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private Long size;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.extra
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private String extra;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.url
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private String url;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.children
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private Integer children;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.level
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private Integer level = 0;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.user_id
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.parent_id
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private Long parentId;

    /**
     *
     * status 主要用于分享文件状态, 如果等于0表示为私有不公开(选中未状态), 1 为公开分享(选中状态),
     *          2 为当文件夹里存在选中与未选中时的半选中状态
     *
     * @mbg.generated Mon Feb 13 15:03:37 CST 2017
     */
    private Byte status = 0;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.created
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private Date created;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_file.updated
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.id
     *
     * @return the value of tb_file.id
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.id
     *
     * @param id the value for tb_file.id
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.name
     *
     * @return the value of tb_file.name
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.name
     *
     * @param name the value for tb_file.name
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getStorageMethod() {
        return storageMethod;
    }

    public void setStorageMethod(String storageMethod) {
        this.storageMethod = storageMethod;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.attraction
     *
     * @return the value of tb_file.attraction
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public String getAttraction() {
        return attraction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.attraction
     *
     * @param attraction the value for tb_file.attraction
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setAttraction(String attraction) {
        this.attraction = attraction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.worth
     *
     * @return the value of tb_file.worth
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Long getWorth() {
        return worth;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.worth
     *
     * @param worth the value for tb_file.worth
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setWorth(Long worth) {
        this.worth = worth;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.type
     *
     * @return the value of tb_file.type
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.type
     *
     * @param type the value for tb_file.type
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.size
     *
     * @return the value of tb_file.size
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Long getSize() {
        return size;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.size
     *
     * @param size the value for tb_file.size
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.extra
     *
     * @return the value of tb_file.extra
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public String getExtra() {
        return extra;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.extra
     *
     * @param extra the value for tb_file.extra
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setExtra(String extra) {
        this.extra = extra;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.url
     *
     * @return the value of tb_file.url
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.url
     *
     * @param url the value for tb_file.url
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.children
     *
     * @return the value of tb_file.children
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Integer getChildren() {
        return children;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.children
     *
     * @param children the value for tb_file.children
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setChildren(Integer children) {
        this.children = children;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.level
     *
     * @return the value of tb_file.level
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.level
     *
     * @param level the value for tb_file.level
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.user_id
     *
     * @return the value of tb_file.user_id
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.user_id
     *
     * @param userId the value for tb_file.user_id
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.parent_id
     *
     * @return the value of tb_file.parent_id
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.parent_id
     *
     * @param parentId the value for tb_file.parent_id
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.status
     *
     * @return the value of tb_file.status
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.status
     *
     * @param status the value for tb_file.status
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.created
     *
     * @return the value of tb_file.created
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.created
     *
     * @param created the value for tb_file.created
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_file.updated
     *
     * @return the value of tb_file.updated
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_file.updated
     *
     * @param updated the value for tb_file.updated
     *
     * @mbg.generated Mon Feb 27 12:22:58 CST 2017
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}