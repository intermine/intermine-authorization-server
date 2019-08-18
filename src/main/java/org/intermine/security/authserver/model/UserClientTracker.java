package org.intermine.security.authserver.model;

import javax.persistence.*;

/**
 * Model class for user_client_tracker table.
 *
 * @author Rahul Yadav
 *
 */
@Entity
@Table(name = "user_client_tracker")
public class UserClientTracker {

    /**
     * Construct an userClientTracker instance
     */
    protected UserClientTracker() {}

    /**
     * Construct an userClientTracker instance
     *
     * @param username unique username of the user
     * @param clientName name of the client
     * @param merged merged status of the old account on the client
     */
    public UserClientTracker(String username, String clientName, Boolean merged) {
        this.username = username;
        this.clientName = clientName;
        this.merged=merged;
    }

    /**
     *Auto generated id for row.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    /**
     *Unique username of the user
     */
    @Column(name = "userName",nullable = false)
    private String username;

    /**
     *Name of the client, user is authenticated on.
     */
    @Column(name = "clientName",nullable = false)
    private String clientName;

    /**
     *Merge status of user with old mine account
     */
    @Column(name = "merged")
    private boolean merged;

    /**
     * <p>Used to get the id of row.
     * </p>
     *
     * @return Long unique id of the row
     */
    public Long getId() {
        return id;
    }

    /**
     * <p>Used to set the unique id of the row
     * client.
     * </p>
     *
     * @param id id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p>Used to get the username of the user
     * </p>
     *
     * @return String unique username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Used to set the username in the row.
     * </p>
     *
     * @param username username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <p>Used to get the client Name.
     * </p>
     *
     * @return String name of the client
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * <p>Used to set the client name in the row.
     * </p>
     *
     * @param clientName name of the client
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * <p>Used to set the merged status of the user old
     * mine account.
     * </p>
     *
     * @param merged status to update
     */
    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    /**
     * <p>Used to check merge status of user with the mine
     * old account.
     * </p>
     *
     * @return boolean true if merged else false
     */
    public boolean isMerged() {
        return merged;
    }


    /**
     * <p>Convert fields in to string.
     * </p>
     *
     * @return String converted string
     */
    @Override
    public String toString() {
        return String.format(
                "UserClientTracker[id=%d, user='%s', client='%s', merged='%d']",
                id, username, clientName, merged);
    }
}
