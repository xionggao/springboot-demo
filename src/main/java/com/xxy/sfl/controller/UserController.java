package com.xxy.sfl.controller;

import com.xxy.sfl.entity.UserEntity;
import com.xxy.sfl.pub.controller.BaseRestController;
import com.xxy.sfl.service.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "user")
public class UserController extends BaseRestController<IUserService, UserEntity> {
}
