package org.intermine.security.authserver.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "permission")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }
}
