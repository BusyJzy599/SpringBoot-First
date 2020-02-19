package com.myworld.test.demo.mapper;

import com.myworld.test.demo.model.User;
import org.apache.ibatis.annotations.*;

/**
 *
 */
@Mapper
public interface UserMapper {
    //mybatis将会把User的对象user里面属性填充到对应的#{属性名}里去
    @Insert("insert into user (account_id,name,token,gmt_create,gmt_modified,bio) values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{bio})")
    void insert(User user);
    //当参数不为对象时，需要加一个注解
    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where account_id=#{accountId}")
    User findById(@Param("accountId") Integer accountId);

    @Select("select * from user where account_id=#{accountId}")
    Integer checkById(@Param("accountId")Integer accountId);

    @Update("update user set name=#{name},token=#{token},gmt_create=#{gmtCreate},gmt_modified=#{gmtModified},bio=#{bio} where account_id=#{accountId}")
    void updateUser(User user);
}
