package com.sb.demo.repo;

import org.springframework.stereotype.Repository;

import com.sb.demo.entity.StudentEntity;
import com.sb.demo.pub.repo.BaseJpaRepository;

@Repository
public interface StudentRepo extends BaseJpaRepository<StudentEntity> {

}
