package org.intermine.security.authserver.model;


import lombok.Data;

import javax.persistence.*;

/**
 * Model class for permission table.
 *
 * @author Rahul Yadav
 *
 */
@Entity
@Table(name = "permission")
@Data
public class Permission {

    /**
     *Auto generated row id of table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     *Name of the permission.
     */
    @Column(name = "name")
    private String name;

    /**
     * <p>Used to get the name of permission.
     * </p>
     *
     * @return String name of permission
     */
    public String getName() {
        return name;
    }
}
