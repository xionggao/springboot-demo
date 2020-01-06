package com.xxy.sfl.service;

import com.xxy.sfl.repo.StudentRepo;
import org.springframework.stereotype.Service;

import com.xxy.sfl.entity.StudentEntity;
import com.xxy.sfl.pub.service.CommonServiceImpl;

@Service
public class StudentServiceImpl extends CommonServiceImpl<StudentRepo, StudentEntity> implements IStudentService {

}
