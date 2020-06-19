package com.kang.mapper.slave;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Update;

public interface SlaveUserMapper {

	//@Select("select * from user")
	List<Map<String,Object>> getUserAll();
	
	@Update("update tb_user set tel = 22222 where id = 2")
	int updateUser();
}
