package com.myworld.test.demo.mapper;

import com.myworld.test.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
