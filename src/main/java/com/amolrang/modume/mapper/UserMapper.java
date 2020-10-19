package com.amolrang.modume.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import com.amolrang.modume.model.UserModel;

@Mapper
public interface UserMapper {
	@Select("SELECT * FROM USER_INFO WHERE id=#{id}")
	UserModel readUser(String id);
	
	@Insert("INSERT INTO user_info(id, password, isAccountNonexpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, username)(select #{id}, #{password}, #{isAccountNonExpired}, #{isAccountNonLocked}, #{isCredentialsNonExpired}, #{isEnabled}, #{username})")
	int insertUser(UserModel userModel);
	
	@Insert("INSERT INTO AUTHORITY VALUES(#{id},#{autority})")
	int insertUserAutority(@Param("id") String id, @Param("autority") String autority);
	
	@Select("SELECT autority FROM AUTHORITY WHERE id=#{id}")
	List<String> readautorities(String id);
}
