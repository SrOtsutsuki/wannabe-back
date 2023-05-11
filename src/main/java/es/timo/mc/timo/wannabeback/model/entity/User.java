package es.timo.mc.timo.wannabeback.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type User.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "WANNABE_USER")
public class User implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -8555073340260595731L;

    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Username.
     */
    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    /**
     * The Password.
     */
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    /**
     * The Roles.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();


    /**
     * The Created date.
     */
    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    private Date createdDate;

    /**
     * The Modified date.
     */
    @LastModifiedDate
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;


}
