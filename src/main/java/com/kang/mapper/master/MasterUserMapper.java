package com.kang.mapper.master;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Update;


public interface MasterUserMapper {

	//@Select("select * from tb_user")
	List<Map<String,Object>> getUserAll();

	@Update("update tb_user set tel = 33333 where id = 2")
	int updateUser();
}