package org.intermine.security.authserver.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * Model class for role table.
 *
 * @author Rahul Yadav
 *
 */
@Entity
@Table(name = "role")
@Data
public class Role implements Serializable {

    /**
     * Role name for normal user.
     */
    public static final String ROLE_USER = "ROLE_USER";

    /**
     * Role name for admin user.
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     *Auto generated row id of table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     *Name of the role going to store in table
     */
    @Column(name = "name")
    private String name;

    /**
     *Role permission mapping
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "permission_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "id")})

    private List<Permission> permissions;

    /**
     * <p>Used to get list of permissions .
     * </p>
     *
     * @return List of permissions
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * <p>Used to get name of the role.
     * </p>
     *
     * @return String name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Used to set name of the role.
     * </p>
     *
     * @param roleName name of the role
     */
    public void setRoleName(String roleName) {
        this.name = roleName;
    }
}
