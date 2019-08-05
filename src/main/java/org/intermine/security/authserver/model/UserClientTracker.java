package org.intermine.security.authserver.model;

import javax.persistence.*;

@Entity
@Table(name = "user_client_tracker")
public class UserClientTracker {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name = "userName",nullable = false)
    private String username;
    @Column(name = "clientName",nullable = false)
    private String clientName;
    @Column(name = "merged")
    private boolean merged;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    protected UserClientTracker() {}

    public UserClientTracker(String username, String clientName, Boolean merged) {
        this.username = username;
        this.clientName = clientName;
        this.merged=merged;
    }

    public boolean isMerged() {
        return merged;
    }


    @Override
    public String toString() {
        return String.format(
                "UserClientTracker[id=%d, user='%s', client='%s', merged='%d']",
                id, username, clientName, merged);
    }
}
