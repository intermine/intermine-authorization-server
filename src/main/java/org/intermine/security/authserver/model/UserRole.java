package org.intermine.security.authserver.model;


import javax.persistence.*;

/**
 * Model class for role_user table.
 *
 * @author Rahul Yadav
 *
 */
@Entity
@Table(name = "role_user")
public class UserRole {

    /**
     *Auto generated row id of table.
     */
    @Id
    @GeneratedValue
    @Column(name = "row_id", nullable = false)
    private Long rowId;

    /**
     *Mapping of role id with role names in role_user
     * table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Role role;

    /**
     * <p>Used to get row id from table.
     * </p>
     *
     * @return Long unique row id
     */
    public Long getRowId() {
        return rowId;
    }

    /**
     * <p>Used to set row id in table.
     * </p>
     *
     * @param rowId id to set
     */
    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    /**
     * <p>Used to get user from table.
     * </p>
     *
     * @return users object of Users model class
     */
    public Users getUsers() {
        return users;
    }

    /**
     * <p>Used to set user.
     * </p>
     *
     * @param users instance of Users model class
     */
    public void setUsers(Users users) {
        this.users = users;
    }

    /**
     * <p>Used to get role from table.
     * </p>
     *
     * @return role Object of Role model class
     */
    public Role getRole() {
        return role;
    }

    /**
     * <p>Used to set role in table.
     * </p>
     *
     * @param role instance of Role model class
     */
    public void setRole(Role role) {
        this.role = role;
    }

}
