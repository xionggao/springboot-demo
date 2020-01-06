package com.xxy.sfl.service.impl;

import com.xxy.sfl.entity.UserEntity;
import com.xxy.sfl.pub.service.CommonServiceImpl;
import com.xxy.sfl.repo.UserRepo;
import com.xxy.sfl.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CommonServiceImpl<UserRepo, UserEntity> implements IUserService {
}
