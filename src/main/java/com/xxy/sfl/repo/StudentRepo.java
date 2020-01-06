package com.xxy.sfl.repo;

import org.springframework.stereotype.Repository;

import com.xxy.sfl.entity.StudentEntity;
import com.xxy.sfl.pub.repo.BaseJpaRepository;

@Repository
public interface StudentRepo extends BaseJpaRepository<StudentEntity> {

}
