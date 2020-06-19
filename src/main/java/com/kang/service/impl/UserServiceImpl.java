package com.kang.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kang.common.msg.ErrorCode;
import com.kang.common.msg.Message;
import com.kang.mapper.master.MasterUserMapper;
import com.kang.mapper.slave.SlaveUserMapper;
import com.kang.service.UserService;

import cn.hutool.core.lang.Dict;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private MasterUserMapper masterUserMapper;
	
	@Autowired
	private SlaveUserMapper slaveUserMapper;

	@Transactional
	@Override
	public Message<?> updateUser() {
		masterUserMapper.updateUser();
//		int i = 1 / 0;
		slaveUserMapper.updateUser();
        return new Message<>(ErrorCode.SUCCESS);
	}

	@Override
	public Message<?> getUserList() {
		List<Map<String, Object>> masterUserList = masterUserMapper.getUserAll();
		List<Map<String, Object>> slaveUserList = slaveUserMapper.getUserAll();
		Dict userList = Dict.create().set("masterUserList", masterUserList).set("slaveUserList", slaveUserList);
		return new Message<>(ErrorCode.SUCCESS,userList);
	}

}
