package es.timo.mc.timo.wannabeback.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * The type Person.
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
public class Person implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -4955185306027026912L;

    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * The Name.
     */
    @Column(nullable = false, name = "NAME")
    private String name;

    /**
     * The Mail.
     */
    @Column(nullable = false, name = "MAIL")
    private String mail;

    /**
     * The Phone.
     */
    @Column(nullable = false, name = "PHONE")
    private String phone;

    /**
     * The Document.
     */
    @Column(nullable = false, name = "DOCUMENT", unique = true)
    private String document;

    /**
     * The Accept lopd.
     */
    @Column(nullable = false, name = "LOPD")
    private Boolean acceptLOPD = false;

    /**
     * The Accept comercial info.
     */
    @Column(nullable = false, name = "COMERCIAL_INFO")
    private Boolean acceptComercialInfo = false;

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


    /**
     * Equals boolean.
     *
     * @param o the o
     * @return the boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Person person = (Person) o;
        return id != null && Objects.equals(id, person.id);
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
