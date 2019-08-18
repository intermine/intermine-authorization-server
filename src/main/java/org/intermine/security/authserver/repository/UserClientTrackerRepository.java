package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.UserClientTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Jpa repository to query user_client_tracker table
 * with the help of UserClientTracker model.
 *
 * @author Rahul Yadav
 *
 */
@Repository
public interface UserClientTrackerRepository extends JpaRepository<UserClientTracker,Long> {

    /**
     * <p>Query the table to update merged entry of table with the
     * help of clientName and username
     *  </p>
     *
     * @param clientName registered clientName
     * @param username user
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE UserClientTracker c SET c.merged = :merged WHERE c.clientName = :clientName and c.username= :username")
    void updateMerged(@Param("clientName") String clientName, @Param("username") String username, @Param("merged") boolean merged);

    /**
     * <p>Query the table and find unique entry with the help of
     * clientName and username.
     *  </p>
     *
     * @param clientName registered client name
     * @param  username authenticated user
     * @return UserClientTracker model
     */
    UserClientTracker findByClientNameAndAndUsername(String clientName, String username);

}
