package es.timo.mc.timo.wannabeback.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
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
 * The type Ticket.
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
public class Ticket implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -3428785362229123548L;

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
     * The Description.
     */
    @Lob
    @Column(nullable = false, name = "DECRIPTION")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    /**
     * The Short description.
     */
    @Column(nullable = false, name = "SHORT_DESCRIPTION")
    private String shortDescription;


    /**
     * The Main pic.
     */
    @ManyToOne
    @JoinColumn(name = "FK_MAIN_PIC_ID")
    private Image mainPic;


    /**
     * The Ticket pic.
     */
    @ManyToOne
    @JoinColumn(name = "FK_TICKET_PIC_ID")
    private Image ticketPic;


    /**
     * The Reserve pic.
     */
    @ManyToOne
    @JoinColumn(name = "FK_RESERVE_PIC_ID")
    private Image reservePic;

    /**
     * The Capacity.
     */
    @Column(nullable = false, name = "CAPACITY")
    private Integer capacity;

    /**
     * The Price.
     */
    @Column(nullable = false, name = "PRICE")
    private Double price;

    /**
     * The Date.
     */
    @Column(nullable = false, name = "DATE")
    private Date date;

    /**
     * The End sale date.
     */
    @Column(nullable = false, name = "END_SALE_DATE")
    private Date endSaleDate;

    /**
     * The Start sale date.
     */
    @Column(nullable = false, name = "START_SALE_DATE")
    private Date startSaleDate;

    /**
     * The Closing date.
     */
    @Column(nullable = false, name = "CLOSING_DATE")
    private Date closingDate;

    /**
     * The Capacity reserve.
     */
    @Formula("(select count(*) from Purchase p, Reserve r where p.fk_ticket_id = id and p.id = r.fk_purchase_id and r.canceled = false and p.estate = 'PAID' )")
    private Integer capacityReserve;

    /**
     * The Business.
     */
    @ManyToOne
    @JoinColumn(name = "FK_BUSSINESS_ID", nullable = false, updatable = false)
    private Business business;

    /**
     * The Coupons.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "tickets")
    private List<Coupon> coupons;

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
        Ticket ticket = (Ticket) o;
        return id != null && Objects.equals(id, ticket.id);
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

    /**
     * Remove ticket from coupon.
     */
    @PreRemove
    private void removeTicketFromCoupon() {
        for (Coupon c : coupons) {
            c.getTickets().remove(this);
        }
    }
}
