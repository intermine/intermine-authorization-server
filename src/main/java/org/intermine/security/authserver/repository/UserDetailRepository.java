package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<Users,Integer> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Users c SET c.password = :password WHERE c.username = :username")
    void updatePassword(@Param("password") String password, @Param("username") String username);
    Users findByUsername(String name);
    Users findByUserId(Integer userId);
}
