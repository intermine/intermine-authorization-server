package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Jpa repository to query users table
 * with the help of Users model.
 *
 * @author Rahul Yadav
 *
 */
public interface UserDetailRepository extends JpaRepository<Users,Integer> {

    /**
     * <p>Query the table to update old password with the new
     * one.
     *  </p>
     *
     * @param password new password
     * @param username who request to update password
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Users c SET c.password = :password WHERE c.username = :username")
    void updatePassword(@Param("password") String password, @Param("username") String username);

    /**
     * <p>Query the table and find unique user with username
     *  </p>
     *
     * @param name username of user
     * @return Users model
     */
    Users findByUsername(String name);

    /**
     * <p>Query the table and find unique user with userId
     *  </p>
     *
     * @param userId userId of user
     * @return Users model
     */
    Users findByUserId(Integer userId);
}
