package com.kang.service;

import com.kang.common.msg.Message;

public interface UserService {
	
	/**
	 * <p>Title: getUserList</p>
	 * <p>Description: 用户列表</p>
	 * @param @return
	 */
	Message<?> getUserList();

	/**
	 * <p>Title: updateUser</p>
	 * <p>Description: 更新用户</p>
	 * @param @return
	 */
	Message<?> updateUser();
}
