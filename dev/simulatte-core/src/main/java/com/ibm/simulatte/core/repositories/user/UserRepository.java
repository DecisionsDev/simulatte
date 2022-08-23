package com.ibm.simulatte.core.repositories.user;

import com.ibm.simulatte.core.datamodels.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUid(int uid);


}
