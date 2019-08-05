package org.intermine.security.authserver.repository;

import org.intermine.security.authserver.model.UserClientTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserClientTrackerRepository extends JpaRepository<UserClientTracker,Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE UserClientTracker c SET c.merged = :merged WHERE c.clientName = :clientName and c.username= :username")
    void updateMerged(@Param("clientName") String clientName, @Param("username") String username, @Param("merged") boolean merged);

    UserClientTracker findByClientNameAndAndUsername(String clientName, String username);

}
