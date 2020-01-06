package com.xxy.sfl.repo;

import com.xxy.sfl.entity.UserEntity;
import com.xxy.sfl.pub.repo.BaseJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends BaseJpaRepository<UserEntity> {
    
}
