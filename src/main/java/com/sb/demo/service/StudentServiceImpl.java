package com.sb.demo.service;

import org.springframework.stereotype.Service;

import com.sb.demo.entity.StudentEntity;
import com.sb.demo.pub.service.CommonServiceImpl;
import com.sb.demo.repo.StudentRepo;

@Service
public class StudentServiceImpl extends CommonServiceImpl<StudentRepo, StudentEntity> implements IStudentService {

}
