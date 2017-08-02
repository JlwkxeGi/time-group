package mycourse.onkshare.mapper;

import java.util.List;
import mycourse.onkshare.model.LoginLog;
import mycourse.onkshare.model.LoginLogExample;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface LoginLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    long countByExample(LoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    int deleteByExample(LoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    int insert(LoginLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    int insertSelective(LoginLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    List<LoginLog> selectByExample(LoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    LoginLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    int updateByExampleSelective(@Param("record") LoginLog record, @Param("example") LoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    int updateByExample(@Param("record") LoginLog record, @Param("example") LoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    int updateByPrimaryKeySelective(LoginLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_login_log
     *
     * @mbg.generated Tue Apr 11 20:24:17 CST 2017
     */
    int updateByPrimaryKey(LoginLog record);

    List<LoginLog> selectFrontFew(Long userId);
}