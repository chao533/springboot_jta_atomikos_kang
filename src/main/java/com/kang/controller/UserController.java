package com.kang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kang.common.msg.Message;
import com.kang.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value = "/userList",method=RequestMethod.GET)
    public Message<?> userList() {
        return userService.getUserList();
    }
	
	@RequestMapping(value = "/updateUser",method=RequestMethod.POST)
    public Message<?> updateUser() {
        return userService.updateUser();
    }
}
