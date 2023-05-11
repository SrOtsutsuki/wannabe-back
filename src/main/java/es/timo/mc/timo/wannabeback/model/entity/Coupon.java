package es.timo.mc.timo.wannabeback.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The type Coupon.
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
public class Coupon implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 3665789885702040530L;

    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Code.
     */
    @Column(name = "CODE", nullable = false)
    private String code;

    /**
     * The Valid since.
     */
    @Column(name = "valid_since", nullable = false)
    private Date validSince;

    /**
     * The Valid until.
     */
    @Column(name = "valid_until", nullable = false)
    private Date validUntil;

    /**
     * The Discount.
     */
    @Column(name = "DISCOUNT", nullable = false)
    private Double discount;

    /**
     * The Consummation.
     */
    @Column(name = "CONSUMATION", nullable = false)
    private Boolean consummation;

    /**
     * The Max applications.
     */
    @Column(name = "MAX_APPLICATIONS", nullable = false)
    private Integer maxApplications;

    /**
     * The Total people per use.
     */
    @Column(name = "TOTAL_PEOPLE_PER_USE", nullable = false)
    private Integer totalPeoplePerUse;

    /**
     * The Capacity reserve.
     */
    @Formula("(select count(*) from Purchase p where p.fk_coupon_id = id)")
    private Integer applications;

    /**
     * The Tickets.
     */
    @JoinTable(
            name = "COUPON_TICKET",
            joinColumns = @JoinColumn(name = "FK_COUPON", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "FK_TICKET", nullable = false)
    )
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Ticket> tickets;

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
        Coupon coupon = (Coupon) o;
        return id != null && Objects.equals(id, coupon.id);
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
