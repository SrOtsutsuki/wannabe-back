package es.timo.mc.timo.wannabeback.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * The type Reserve.
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
public class Reserve implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -9117535105887087347L;

    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Reserve code.
     */
    @Column(name = "RESERVE_CODE", unique = true, length = 8, nullable = false, updatable = false)
    private String reserveCode;

    /**
     * The Order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_PURCHASE_ID", nullable = false, updatable = false)
    @JsonBackReference
    private Purchase purchase;

    /**
     * The Person.
     */
    @ManyToOne
    @JoinColumn(name = "FK_PERSON_ID", nullable = false, updatable = false)
    private Person person;

    /**
     * The Used.
     */
    @Column(name = "USED")
    private Boolean used = Boolean.FALSE;

    @Column(name = "CANCELED")
    private Boolean canceled = Boolean.FALSE;

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
        Reserve reserve = (Reserve) o;
        return id != null && Objects.equals(id, reserve.id);
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
